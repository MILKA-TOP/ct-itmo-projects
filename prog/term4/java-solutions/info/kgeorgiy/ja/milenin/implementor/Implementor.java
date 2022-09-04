package info.kgeorgiy.ja.milenin.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;


/**
 * This class can create realisations of interfaces.
 * <p>
 * Implementing {@link Impler} and {@link JarImpler}
 *
 * @author MILKA
 */
public class Implementor implements Impler, JarImpler {


    /**
     * Function, which return part of method's argument like {@code (ClassName argumentName)}.
     */
    private static final Function<Parameter, String> FULL_ARGUMENT = arg -> arg.getType().getCanonicalName() + " " + arg.getName();


    /**
     * Empty constructor of an instance of the class.
     */
    public Implementor() {
    }


    /**
     * Start implement {@code interface} to {@code path} or create .jar of compiled {@code interface}
     * to  {@code path.jar}
     *
     * @param args Array of arguments containing {@code [token, root]} or {@code [-jar, token, root]}, where
     *             <p>
     *             {@code token} - interface that will be implemented;
     *             <p>
     *             {@code root} - path of root or .jar directory.
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2 || (args.length == 3 && !args[0].equals("-jar")) || args.length > 3) {
            System.err.println(errorMessage("Please, input `Implementor [token] [root]` or `Implementor -jar [token] [root]`"));
            return;
        }

        int isJar = args.length == 3 ? 1 : 0;
        try {
            Class<?> token = Class.forName(args[isJar]);
            Path root = Path.of(args[1 + isJar]);
            if (isJar == 1) {
                new info.kgeorgiy.ja.milenin.implementor.Implementor().implementJar(token, root);
            } else {
                new info.kgeorgiy.ja.milenin.implementor.Implementor().implement(token, root);
            }
        } catch (final ClassNotFoundException e) {
            System.err.println(errorMessage("Unknown class: " + args[isJar]));
        } catch (final InvalidPathException e) {
            System.err.println(errorMessage("Unknown path: " + args[1 + isJar]));
        } catch (final ImplerException e) {
            System.err.println(errorMessage("Caught exception: "));
        }
    }

    /**
     * Return formatted error-message of the form {@code [ERROR]::::[errorMessage]}
     *
     * @param message errorMessage
     * @return {@link String} formatted error-message.
     */
    private static String errorMessage(final String message) {
        return String.format("[ERROR]::::[%s]", message);
    }


    /**
     * Implement {@link Class token} with suffix "Impl" and put compiled classes to {@link Path jarFile}.
     *
     * @param token   interface, which will be implemented;
     * @param jarFile path of .jar, where will be compiled {@link Class token};
     * @throws ImplerException if you will have some problems with creating temp-directories, implementing,
     *                         compiling or creating .jar file;
     */
    @Override
    public void implementJar(final Class<?> token, final Path jarFile) throws ImplerException {
        Path fullPathParent = jarFile.toAbsolutePath().getParent();
        Path tempDirectory = null;
        if (fullPathParent != null) {
            try {
                tempDirectory = Files.createTempDirectory(fullPathParent, "tempDir");
            } catch (final IOException | SecurityException e) {
                throw new ImplerException(errorMessage("Can't create tempDir at this path: " + fullPathParent));
            }
        }
        try {
            implement(token, tempDirectory);
            compileWorker(token, tempDirectory);
            jarWorker(token, jarFile, tempDirectory);
        } finally {
            deleteDirectoryWorker(tempDirectory);
        }
    }

    /**
     * Delete all files or directories, which contains in {@link Path directory}, include this {@code directory}.
     *
     * @param directory Path, where will be removed all files or directories;
     */
    private void deleteDirectoryWorker(final Path directory) {
        try {
            Files.walk(directory).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    System.err.println(errorMessage("Can't delete this path: " + path));
                }
            });
        } catch (IOException e) {
            System.err.println(errorMessage("Can't delete temp-directory"));
        }
    }

    /**
     * Create .jar file in the {@link Path mainJarPath} of compiled {@link Class token} with suffix "Impl".
     *
     * @param token         class, which was implemented with suffix "Impl";
     * @param mainJarPath   path, where will be .jar file of compiled implemented {@code token};
     * @param tempDirectory {@link Path} of temp directory, which contain implemented and
     *                      compiled {@link Class token} with suffix "Impl";
     * @throws ImplerException if can't create .jar file;
     */
    private void jarWorker(final Class<?> token, final Path mainJarPath, final Path tempDirectory) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(mainJarPath), manifest)) {
            String nowName = getFullNameClass(token).replace('.', '/') + "Impl.class";
            jos.putNextEntry(new ZipEntry(nowName));
            Files.copy(implClassPath(tempDirectory, token, "Impl.class"), jos);
        } catch (IOException e) {
            throw new ImplerException(errorMessage("Can't create .jar file"));
        }
    }


    /**
     * Compiling realization of {@link Class token} with suffix "Impl", which contains in {@link Path root}.
     *
     * @param token interface, which was implemented with suffix "Impl";
     * @param root  path, where contains implemented interface with suffix "Impl";
     * @throws ImplerException if {@link JavaCompiler compiler} is null or {@code compilerCode != 0} ;
     */
    public void compileWorker(final Class<?> token, final Path root) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException(errorMessage("Could not find java compiler"));
        }
        final String classpath = root.toString() + File.pathSeparator + getClassPath(token);
        final String[] args = new String[]{
                "-cp",
                classpath,
                "-encoding",
                "utf8",
                implClassPath(root, token, "Impl.java").toString()};
        if (compiler.run(null, null, null, args) != 0) {
            throw new ImplerException(errorMessage("Compiler exit code"));
        }
    }


    /**
     * Returns class-path of {@link Class toke}
     *
     * @param token class, want to get class-path from;
     * @return {@link String} class-path of {@link Class toke}
     */
    private String getClassPath(final Class<?> token) {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }


    /**
     * Implement {@link Class aClass} with suffix "Impl" and put this file to {@link Path path}.
     *
     * @param aClass interface, which will be implemented with suffix "Impl";
     * @param path   path of directory, where will be realisation of {@link Class aClass};
     * @throws ImplerException if {@link Class aClass} is not an interface or {@link Path path} is not correct or
     *                         problems with writing implemented methods in the file;
     */
    @Override
    public void implement(final Class<?> aClass, Path path) throws ImplerException {

        checkInputClass(aClass);
        path = pathWorker(path, aClass);

        try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(correctStringConverter(headerWriter(aClass)));

            for (Method nowMethod : aClass.getMethods()) {
                if (Modifier.isAbstract(nowMethod.getModifiers())) {
                    writer.write(correctStringConverter(fullMethodWriter(nowMethod)));
                }
            }

            writer.write('}');
        } catch (final IOException e) {
            throw new ImplerException(errorMessage("Can't write at this file " + path));
        }
    }

    /**
     * Transform {@link String inputLine} with incorrect symbols to standard Unicode form.
     *
     * @param inputLine line, which will be transformed;
     * @return correct line without unknown symbols;
     */
    private String correctStringConverter(String inputLine) {
        StringBuilder outputLine = new StringBuilder();
        for (char symbol : inputLine.toCharArray()) {
            outputLine.append((symbol >= 128) ? String.format("\\u%04x", (int) symbol) : symbol);
        }
        return outputLine.toString();
    }


    /**
     * Returns full name of class with his package, if there is one.
     *
     * @param aClass class, whose full name is return;
     * @return {@link String} full name of {@link Class aClass};
     */
    private String getFullNameClass(final Class<?> aClass) {
        String packageName = aClass.getPackageName();
        String className = aClass.getSimpleName();
        return packageName.isEmpty() ? className : packageName + "." + className;
    }

    /**
     * Check, that {@link Class aClass} is correct interface or throw {@link ImplerException};
     *
     * @param aClass class, that is checked for correctness;
     * @throws ImplerException if {@link Class aClass} isn't correct interface;
     */
    private void checkInputClass(final Class<?> aClass) throws ImplerException {
        if (!aClass.isInterface() || Modifier.isPrivate(aClass.getModifiers())) {
            throw new ImplerException(errorMessage(aClass.getName() + " is not an interface"));
        }
    }

    /**
     * Returns the implemented {@link Method nowMethod} for form:
     * {@code
     * {@Override METHOD_INFO {
     * return RETURN_TYPE;
     * }}}
     *
     * @param nowMethod Method, which will be realized;
     * @return {@link String} full realized {@link Method nowMethod}
     */
    private String fullMethodWriter(final Method nowMethod) {
        return String.format("\t@Override%n\t%s{%n\t\treturn%s;%n\t}%n%n", infoMethodWriter(nowMethod), returnMethodWriter(nowMethod));
    }


    /**
     * Returns in {@link String} default values.
     *
     * @param nowMethod method, to return the default value
     * @return {@link String} default value of {@link Method nowMethod}
     */
    private String returnMethodWriter(final Method nowMethod) {
        if (nowMethod.getReturnType().equals(void.class)) {
            return "";
        } else if (!nowMethod.getReturnType().isPrimitive()) {
            return " null";
        } else if (nowMethod.getReturnType().equals(boolean.class)) {
            return " false";
        } else {
            return " 0";
        }
    }


    /**
     * Create first line of method.
     * {@code
     * [METHOD_MODIFIERS] [METHOD_RETURN_TYPE] METHOD_NAME (arg0, arg1, ...) throws EX_1, EX_2,... }
     *
     * @param nowMethod Method, which will be realized;
     * @return {@link String} first line of realized {@link Method nowMethod}
     */
    private String infoMethodWriter(final Method nowMethod) {
        String methodMod = Modifier.toString(nowMethod.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT);
        String methodType = nowMethod.getReturnType().getCanonicalName();
        String methodName = nowMethod.getName();
        return String.format("%s %s %s(%s) %s", methodMod, methodType, methodName, argsMethodWriter(nowMethod), throwsMethodWriter(nowMethod.getExceptionTypes()));
    }

    /**
     * Create {@link String} sequence of some {@link Arrays params}, which converted by {@link Function function}
     * and written with ','.
     *
     * @param params   some arguments, which will be written with ',';
     * @param function some operation, which convert {@link Arrays params};
     * @param <T>      type of {@link Arrays params} array;
     * @return {@link String} sequence of converted {@link Arrays params} with ',';
     */
    private <T> String streamParams(final T[] params, final Function<T, String> function) {
        return Arrays.stream(params).map(function).collect(Collectors.joining(", "));
    }

    /**
     * Create a sequence of exceptions to the form {@code throws EX_1, EX_2, ...} if {@link Arrays exceptionTypes}
     * is not empty or an empty line in the opposite.
     *
     * @param exceptionTypes {@link Arrays} of method's exceptions;
     * @return {@link String} sequence of exceptions to the form {@code throws EX_1, EX_2, ...} or empty line;
     */
    private String throwsMethodWriter(final Class<?>[] exceptionTypes) {
        return (exceptionTypes.length == 0) ? "" : "throws " + streamParams(exceptionTypes, Class::getCanonicalName);
    }

    /**
     * Create a sequence of method's parameters, which converted {@link #FULL_ARGUMENT}
     *
     * @param nowMethod method, whose parameters will be returned;
     * @return {@link String} sequence of method's parameters;
     */
    private String argsMethodWriter(final Method nowMethod) {
        return streamParams(nowMethod.getParameters(), FULL_ARGUMENT);
    }


    /**
     * Create {@code header} of realized interface, where {@code header} is
     * {@code
     * PACKAGE_INFO;
     * public class [CLASS_NAME]Impl implements [CLASS_NAME]
     * }
     *
     * @param aClass class, which will be implemented;
     * @return {@link String} header of realized interface;
     */
    private String headerWriter(final Class<?> aClass) {
        String headerFormat = "%s;%n%npublic class %s implements %s {%n%n";
        String packageName = aClass.getPackageName();
        String outputPackagePart = packageName.isEmpty() ? "" : "package " + packageName;
        String simpleName = aClass.getSimpleName();
        String canonName = aClass.getCanonicalName();

        return String.format(headerFormat, outputPackagePart, simpleName + "Impl", canonName);
    }

    /**
     * Create new {@link Path} with adding to {@link Path mainPath} new directories and file's name, which taken
     * from {@link Class aClass} {@code packages} <i>(if {@link Class aClass} has it)</i> and {@code simpleName}.
     *
     * @param mainPath {@link Path} which will be expanded;
     * @param aClass   {@link Class} from which the {@code packageName} and {@code simpleName} will be taken;
     * @param suffix   suffix, which added in the end of the {@link Class aClass's} {@code simpleName};
     * @return {@link Path} expanded path;
     */
    private Path implClassPath(Path mainPath, final Class<?> aClass, final String suffix) {
        Package classPackage = aClass.getPackage();
        if (classPackage != null) {
            mainPath = mainPath.resolve(classPackage.getName().replace('.', File.separatorChar));
        }
        return mainPath.resolve(aClass.getSimpleName() + suffix);
    }

    /**
     * Create new directories required for {@link Class aClass}.
     *
     * @param aClass the {@link Class } for which the necessary directories will be created;
     * @param path   root {@link Path} where the {@link Class aClass} files will be located.
     * @return expanded {@link Path path} with package of {@link Class aClass} (if {@link Class aClass}
     * contains it) and {@code simpleName}.
     * @throws ImplerException if can't create directories;
     */
    private Path pathWorker(Path path, final Class<?> aClass) throws ImplerException {
        try {
            path = implClassPath(path, aClass, "Impl.java");

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (final IOException | SecurityException e) {
            throw new ImplerException(errorMessage("Can't create this directory path: " + path));
        }

        return path;

    }

}

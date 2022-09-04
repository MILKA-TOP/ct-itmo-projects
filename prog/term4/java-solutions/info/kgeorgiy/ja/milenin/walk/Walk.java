package info.kgeorgiy.ja.milenin.walk;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Walk {

    private static final String ERROR_FORMAT = "[ERROR]::::[%s]::::[%s]%n";
    private static final String BYTE_ELEMENT = "%02x";
    private static final String WRITER_IO_EXCEPTION_MESSAGE = "WRITER can't complete his task";
    private static final String READER_IO_EXCEPTION_MESSAGE = "READER can't complete his task";
    private static final byte[] FILE_NOT_FOUND_ARRAY = new byte[20];

    public static void main(final String[] args) throws NoSuchAlgorithmException {
        if (!checkArgsSize(args)) {
            return;
        }

        try {
            // :NOTE: 5 исключений
            walkProcess(args[0], args[1]);
        } catch (final NoSuchFileException e) {
            errorShow("Can't find this file: " + e.getFile(), e.toString());
        } catch (final AccessDeniedException e) {
            errorShow("Can't get access to this directory: " + e.getFile(), e.toString());
        } catch (final FileAlreadyExistsException e) {
            errorShow("This file is already exist: " + e.getFile(), e.toString());
        } catch (final InvalidPathException e) {
            errorShow("Can't read this path: " + e.getInput(), e.toString());
        } catch (final IOException e) {
            errorShow("Caught IOException: " + e.getMessage(), e.toString());
        }
    }

    private static void walkProcess(final String inputPath, final String outputPath) throws NoSuchAlgorithmException, InvalidPathException, IOException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");

        final Path sInputFilePath = getPath(inputPath);
        final Path sOutputFilePath = getPath(outputPath);

        if (sOutputFilePath.getParent() != null) {
            Files.createDirectories(sOutputFilePath.getParent());
        }

        // :NOTE: Кодировки
        try (final BufferedReader reader = Files.newBufferedReader(sInputFilePath)) {
            try (final BufferedWriter writer = Files.newBufferedWriter(sOutputFilePath)) {
                try {
                    String checkedPath;
                    final byte[] buffer = new byte[4096];
                    while ((checkedPath = reader.readLine()) != null) {
                        try {
                            writer.write(String.format("%s %s%n", hex(shaAlgorithm(checkedPath, md, buffer)), checkedPath));
                        } catch (final IOException e) {
                            throw checkIOException(e, WRITER_IO_EXCEPTION_MESSAGE);
                        }
                    }
                } catch (final IOException e) {
                    throw checkIOException(e, READER_IO_EXCEPTION_MESSAGE);
                }
            } catch (final IOException e) {
                throw checkIOException(e, WRITER_IO_EXCEPTION_MESSAGE);
            }
        } catch (final IOException e) {
            throw checkIOException(e, READER_IO_EXCEPTION_MESSAGE);
        }
    }

    private static byte[] shaAlgorithm(final String checkedFilePath, final MessageDigest md, final byte[] buffer) {
        try (final InputStream in = Files.newInputStream(Paths.get(checkedFilePath))) {
            md.reset();
            int length;
            while ((length = in.read(buffer)) > 0) {
                md.update(buffer, 0, length);
            }
            return md.digest();
        } catch (final InvalidPathException | IOException e) {
            return FILE_NOT_FOUND_ARRAY;
        }
    }

    private static Path getPath(final String checkedPath) {
        try {
            return Paths.get(checkedPath);
        } catch (final InvalidPathException e) {
            throw new InvalidPathException(checkedPath, e.getMessage());
        }
    }

    private static IOException checkIOException(final IOException e, final String blockExceptionName) {
        return e.getClass().equals(IOException.class) ? new IOException(blockExceptionName) : e;
    }

    private static boolean checkArgsSize(final String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            errorShow("Please, enter `input` and `output` files", "Walk INPUT_FILE OUTPUT_FILE");
            return false;
        }
        return true;
    }

    private static String hex(final byte[] bytes) {
        final StringBuilder resultHex = new StringBuilder();
        for (final byte oneByte : bytes) resultHex.append(String.format(BYTE_ELEMENT, oneByte));

        return resultHex.toString();
    }

    private static void errorShow(final String userMessage, final String systemMessage) {
        System.err.format(ERROR_FORMAT, userMessage, systemMessage);
    }
}

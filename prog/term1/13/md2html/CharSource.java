package md2html;

public interface CharSource {
    boolean hasNext();

    char next();

    char nextTwo();

    char now();

}
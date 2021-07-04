package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    char now();
}
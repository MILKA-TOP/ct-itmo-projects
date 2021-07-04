package expression.exceptions;

public interface CharSource {
    boolean hasNext();

    char next();

    char now();
}
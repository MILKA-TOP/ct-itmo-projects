package expression.exceptions;

public class UnknownModeException extends RuntimeException {
    public UnknownModeException() {
        super("Input unknown mode");
    }
}

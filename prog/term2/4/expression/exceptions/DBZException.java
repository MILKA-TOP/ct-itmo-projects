package expression.exceptions;

public class DBZException extends RuntimeException {

    public DBZException() {
        super("Division by zero");
    }

}

package expression.exceptions;

public class OverflowException extends RuntimeException {

    public OverflowException(String a, String b, String sign) {
        super("Overflew exception " + a + " " + sign + " " + b);
    }

    public OverflowException(String a) {
        super("Overflew exception " + a);
    }

}

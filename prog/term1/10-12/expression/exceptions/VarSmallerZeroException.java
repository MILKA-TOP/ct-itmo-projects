package expression.exceptions;

public class VarSmallerZeroException extends RuntimeException{

    public VarSmallerZeroException() {
        super("Number smaller, than zero");
    }

}

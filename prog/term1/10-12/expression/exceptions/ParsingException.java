package expression.exceptions;

public class ParsingException extends Exception {

    public ParsingException() {
        super("Pars exception");
    }

    public ParsingException(String mes) {
        super(mes);
    }

}

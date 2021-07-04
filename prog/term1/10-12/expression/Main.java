package expression;


import expression.exceptions.*;

public class Main {

    public static void main(String[] args) throws DBZException, OverflowException, ParsingException, VarSmallerZeroException {

        int a = new ExpressionParser().parse("- -2147483648").evaluate(1, 1, 1);
        System.out.println(a);
    }
}

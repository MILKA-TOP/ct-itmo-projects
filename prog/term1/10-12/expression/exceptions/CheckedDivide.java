package expression.exceptions;

import expression.TripleExpression;

public class CheckedDivide extends BinaryOperation {

    public CheckedDivide(TripleExpression first, TripleExpression second) {
        super(first, second, "/");

    }

    @Override
    public int completeFun(int one, int two) throws DBZException, OverflowException {

        long a = one;
        a /= two;
        if (a < Integer.MIN_VALUE || a > Integer.MAX_VALUE) {
            throw new OverflowException(Integer.toString(one), Integer.toString(two), "/");

        }
        return one / two;
    }


}

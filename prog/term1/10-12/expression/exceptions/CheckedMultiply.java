package expression.exceptions;

import expression.TripleExpression;

public class CheckedMultiply extends BinaryOperation {

    TripleExpression first, second;

    public CheckedMultiply(TripleExpression first, TripleExpression second) {
        super(first, second, "*");
    }

    @Override
    public int completeFun(int a, int b) throws DBZException, OverflowException {
        int maximum;
        if (a == 0 || b == 0) {
            return 0;
        }
        if ((a < 0 && b < 0) || (a > 0 && b > 0)) {
            maximum = Integer.MAX_VALUE;
        } else {
            maximum = Integer.MIN_VALUE;
        }
        if (b > a) {
            int temp = a;
            a = b;
            b = temp;
        }
        if ((b > 0 && b > (maximum / a) || (b < 0 && b < maximum / a))) {
            throw new OverflowException(Integer.toString(a), Integer.toString(b), "*");

        }
        return a * b;

    }

}

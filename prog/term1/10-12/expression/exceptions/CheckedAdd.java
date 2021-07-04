package expression.exceptions;

import expression.TripleExpression;

public class CheckedAdd extends BinaryOperation {

    TripleExpression first, second;

    public CheckedAdd(TripleExpression first, TripleExpression second) {
        super(first, second, "+");
    }

    @Override
    public int completeFun(int one, int two) throws OverflowException {

        if ((one >= 0 && Integer.MAX_VALUE - one < two)
                || (one < 0 && Integer.MIN_VALUE - one > two)) {
            throw new OverflowException(Integer.toString(one), Integer.toString(two), "+");

        }


        return one + two;
    }


}

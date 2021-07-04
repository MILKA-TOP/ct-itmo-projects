package expression.exceptions;

import expression.TripleExpression;

public class CheckedAbs extends UnaryOperation {

    public CheckedAbs(TripleExpression first) {
        super(first);
    }

    @Override
    public int completeFun(int one) throws DBZException, OverflowException {

        if (one == Integer.MIN_VALUE) {
            throw new OverflowException(Integer.toString(one));
        } else if (one < 0) {
            one = -one;
        }

        return one;

    }


}

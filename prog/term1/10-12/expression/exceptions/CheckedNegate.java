package expression.exceptions;

import expression.TripleExpression;

public class CheckedNegate extends UnaryOperation {

    TripleExpression first;

    public CheckedNegate(TripleExpression first) {
        super(first);

    }

    @Override
    public int completeFun(int one) throws OverflowException {
        if (one == Integer.MIN_VALUE) {
            throw new OverflowException(Integer.toString(one));
        }
        return -one;
    }


}

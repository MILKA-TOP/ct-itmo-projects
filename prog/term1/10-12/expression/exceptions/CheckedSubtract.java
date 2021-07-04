package expression.exceptions;

import expression.TripleExpression;

public class CheckedSubtract extends BinaryOperation {

    TripleExpression first, second;

    public CheckedSubtract(TripleExpression first, TripleExpression second) {
        super(first, second, "-");
    }

    @Override
    public int completeFun(int one, int two) throws OverflowException {
        int temp = one - two;

        if (((one ^ two) & (one ^ temp)) < 0) {
            throw new OverflowException(Integer.toString(one), Integer.toString(two), "-");
        }
        return one - two;
    }

}

package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public class CheckedMultiply<N> extends BinaryOperation<N> {

    TripleGenExpression<N> first, second;

    public CheckedMultiply(TripleGenExpression<N> first, TripleGenExpression<N> second) {
        super(first, second);
    }

    @Override
    public N completeFun(N one, N two, MathMode<N> mode) throws DBZException, OverflowException {
        return mode.multiply(one, two);

    }

}

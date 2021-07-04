package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public class CheckedSubtract<N> extends BinaryOperation<N> {

    TripleGenExpression<N> first, second;

    public CheckedSubtract(TripleGenExpression<N> first, TripleGenExpression<N> second) {
        super(first, second);
    }

    @Override
    public N completeFun(N one, N two, MathMode<N> mode) throws OverflowException {

        return mode.subtract(one, two);

    }

}

package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public class CheckedNegate<N> extends UnaryOperation<N> {

    public CheckedNegate(TripleGenExpression<N> first) {
        super(first);

    }

    @Override
    public N completeFun(N one, MathMode<N> mode) throws OverflowException {
        return mode.negate(one);

    }


}

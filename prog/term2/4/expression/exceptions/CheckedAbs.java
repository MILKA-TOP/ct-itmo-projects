package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public class CheckedAbs<N> extends UnaryOperation<N> {

    public CheckedAbs(TripleGenExpression<N> first) {
        super(first);
    }

    @Override
    public N completeFun(N one, MathMode<N> mode) throws DBZException, OverflowException {

        return mode.abs(one);

    }


}

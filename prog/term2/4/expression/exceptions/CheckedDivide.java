package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public class CheckedDivide<N> extends BinaryOperation<N> {

    public CheckedDivide(TripleGenExpression<N> first, TripleGenExpression<N> second) {
        super(first, second);

    }

    @Override
    public N completeFun(N one, N two, MathMode<N> mode) throws DBZException, OverflowException {
        return mode.divide(one, two);
    }
}

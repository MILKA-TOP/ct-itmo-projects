package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public abstract class BinaryOperation<N> implements TripleGenExpression<N> {

    private final TripleGenExpression<N> first;
    private TripleGenExpression<N> second;

    public BinaryOperation(TripleGenExpression<N> first, TripleGenExpression<N> second) {
        this.first = first;
        this.second = second;
    }

    protected abstract N completeFun(N one, N two, MathMode<N> mode) throws DBZException, OverflowException;

    @Override
    public N evaluate(N x, N y, N z, MathMode<N> mode) throws DBZException, OverflowException, VarSmallerZeroException {

        return completeFun((first).evaluate(x, y, z, mode), (second).evaluate(x, y, z, mode), mode);

    }
}

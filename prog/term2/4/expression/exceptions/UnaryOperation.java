package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

public abstract class UnaryOperation<N> implements TripleGenExpression<N> {

    private TripleGenExpression<N> first;


    public UnaryOperation(TripleGenExpression<N> first) {
        this.first = first;

    }

    protected abstract N completeFun(N one, MathMode<N> mode) throws DBZException, OverflowException, VarSmallerZeroException;

    @Override
    public N evaluate(N x, N y, N z, MathMode<N> mode) throws DBZException, OverflowException, VarSmallerZeroException {
        return completeFun(first.evaluate(x, y, z, mode), mode);

    }

}

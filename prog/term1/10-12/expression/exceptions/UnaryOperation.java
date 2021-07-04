package expression.exceptions;

import expression.TripleExpression;

public abstract class UnaryOperation implements TripleExpression {

    private TripleExpression first;


    public UnaryOperation(TripleExpression first) {
        this.first = first;

    }

    protected abstract int completeFun(int one) throws DBZException, OverflowException, VarSmallerZeroException;

    @Override
    public int evaluate(int x, int y, int z) throws DBZException, OverflowException, VarSmallerZeroException {
        return completeFun(first.evaluate(x, y, z));

    }

}

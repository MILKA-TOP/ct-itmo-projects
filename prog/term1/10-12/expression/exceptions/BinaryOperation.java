package expression.exceptions;

import expression.ExpressionFunc;
import expression.TripleExpression;

import java.util.Objects;

public abstract class BinaryOperation implements TripleExpression {

    private TripleExpression first;
    private TripleExpression second;
    private String sign;

    public BinaryOperation(TripleExpression first, TripleExpression second, String sign) {
        this.first = first;
        this.second = second;
        this.sign = sign;
    }

    protected abstract int completeFun(int one, int two) throws DBZException, OverflowException;

    @Override
    public int evaluate(int x, int y, int z) throws DBZException, OverflowException, VarSmallerZeroException {
        return completeFun((first).evaluate(x, y, z), (second).evaluate(x, y, z));

    }

}

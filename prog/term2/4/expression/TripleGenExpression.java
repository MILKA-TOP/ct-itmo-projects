package expression;

import expression.MathMode.MathMode;
import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;
import expression.exceptions.VarSmallerZeroException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleGenExpression<N> extends ToMiniString  {

    N evaluate(N x, N y, N z, MathMode<N> mode) throws DBZException, OverflowException, VarSmallerZeroException;

}
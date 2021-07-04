package expression;


import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;
import expression.exceptions.VarSmallerZeroException;

public interface TripleExpression extends ToMiniString  {

    int evaluate(int x, int y, int z) throws DBZException, OverflowException, VarSmallerZeroException;

}

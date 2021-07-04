package expression;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;

public interface Expression extends ToMiniString {

    int evaluate(int x) throws DBZException, OverflowException;

}

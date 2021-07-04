package expression;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;
import expression.exceptions.VarSmallerZeroException;

public class Mines implements TripleExpression {

    private final TripleExpression func;

    public Mines(TripleExpression func) {
        this.func = func;
    }


    @Override
    public int evaluate(int x, int y, int z) throws DBZException, OverflowException, VarSmallerZeroException {
        return func.evaluate(x, y, z) * (-1);
    }

    @Override
    public int hashCode() {
        return func.hashCode() * 32;
    }

    @Override
    public String toString() {
        return "-" + func.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Mines) {
            return ((Mines) obj).func.equals(func);
        } else {
            return false;
        }
    }
}

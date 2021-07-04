package expression.exceptions;

import expression.TripleExpression;

public class CheckedSqrt extends UnaryOperation {

    TripleExpression first;

    public CheckedSqrt(TripleExpression first) {
        super(first);
    }

    @Override
    public int completeFun(int one) throws DBZException, OverflowException, VarSmallerZeroException {
        int out = 1;
        if (one < 0) {
            throw new VarSmallerZeroException();
        } else if (out == 0) {
            return 0;
        }
        while (out <= 46340 && out * out <= one) {
            out++;
        }

        return out - 1;
    }

}

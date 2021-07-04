package expression;

import expression.MathMode.MathMode;

import java.util.Objects;

public class Const<N> implements TripleGenExpression<N> {

    private N num;

    public Const(N x) {
        this.num = x;
    }

    @Override
    public String toString() {
        return num.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            return ((Const) obj).num.equals(num);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }

    @Override
    public N evaluate(N x, N y, N z, MathMode<N> mode) {
        return num;
    }

}

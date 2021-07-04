package expression;

import expression.MathMode.MathMode;

import java.util.Objects;

public class Variable<N> implements TripleGenExpression<N> {
    private String var;

    public Variable(String x) {
        this.var = x;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public N evaluate(N x, N y, N z, MathMode<N> mode) {
        if ("x".equals(var)) {
            return x;
        } else if ("y".equals(var)) {
            return y;
        } else if ("z".equals(var)) {
            return z;
        }
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return ((Variable) obj).var.equals(var);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(var);
    }


}

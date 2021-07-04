package expression;

import java.util.Objects;

public class Variable implements ExpressionFunc {
    private String var;

    public Variable(String x) {
        this.var = x;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (var.equals("x")) {
            return x;
        } else if (var.equals("y")) {
            return y;
        } else if (var.equals("z")) {
            return z;
        } else {
            return -1;
        }
    }

    @Override
    public int evaluate(int num) {
        return num;
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

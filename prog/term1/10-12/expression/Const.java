package expression;

import java.util.Objects;

public class Const implements ExpressionFunc {

    private int num;

    public Const(int x) {
        this.num = x;
    }

    @Override
    public int evaluate(int num) {
        return this.num;
    }

    @Override
    public String toString() {
        return Integer.toString(num);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return num;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            return ((Const) obj).num == num;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}

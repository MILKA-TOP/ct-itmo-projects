package expression.MathMode;

public class CheckedShort implements MathMode<Short> {
    @Override
    public Short add(Short one, Short two) {
        return (short) (one + two);
    }

    @Override
    public Short abs(Short one) {
        return (short) Math.abs(one);
    }

    @Override
    public Short negate(Short one) {
        return (short) -one;
    }

    @Override
    public Short multiply(Short one, Short two) {
        return (short) (one * two);
    }

    @Override
    public Short subtract(Short one, Short two) {
        return (short) (one - two);
    }

    @Override
    public Short divide(Short one, Short two) {
        return (short) (one / two);
    }

    @Override
    public Short getFromNumber(Number one) {
        return one.shortValue();
    }

    @Override
    public Short getFromString(String one) {
        return Short.parseShort(one);
    }
}

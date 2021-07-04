package expression.MathMode;

public class CheckedDouble implements MathMode<Double> {
    @Override
    public Double add(Double one, Double two) {
        return one + two;
    }

    @Override
    public Double abs(Double one) {
        return Math.abs(one);
    }

    @Override
    public Double negate(Double one) {
        return -one;
    }

    @Override
    public Double multiply(Double one, Double two) {
        return one * two;
    }

    @Override
    public Double subtract(Double one, Double two) {
        return one - two;
    }

    @Override
    public Double divide(Double one, Double two) {
        return one / two;
    }

    @Override
    public Double getFromNumber(Number one) {
        return one.doubleValue();
    }

    @Override
    public Double getFromString(String one) {
        return Double.parseDouble(one);
    }
}

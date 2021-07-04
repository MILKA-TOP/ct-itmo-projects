package expression.MathMode;

public class CheckedLong implements MathMode<Long> {
    @Override
    public Long add(Long one, Long two) {
        return one + two;
    }

    @Override
    public Long abs(Long one) {
        return Math.abs(one);
    }

    @Override
    public Long negate(Long one) {
        return -one;
    }

    @Override
    public Long multiply(Long one, Long two) {
        return one * two;
    }

    @Override
    public Long subtract(Long one, Long two) {
        return one - two;
    }

    @Override
    public Long divide(Long one, Long two) {
        return one / two;
    }

    @Override
    public Long getFromNumber(Number one) {
        return one.longValue();
    }

    @Override
    public Long getFromString(String one) {
        return Long.parseLong(one);
    }
}

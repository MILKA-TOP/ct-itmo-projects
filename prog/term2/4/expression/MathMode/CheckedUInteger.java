package expression.MathMode;

public class CheckedUInteger implements MathMode<Integer> {


    @Override
    public Integer add(Integer one, Integer two) {
        return one + two;
    }

    @Override
    public Integer abs(Integer one) {
        return Math.abs(one);
    }

    @Override
    public Integer negate(Integer one) {
        return -one;
    }

    @Override
    public Integer multiply(Integer one, Integer two) {
        return one * two;
    }

    @Override
    public Integer subtract(Integer one, Integer two) {
        return one - two;
    }

    @Override
    public Integer divide(Integer one, Integer two) {
        return one / two;
    }

    @Override
    public Integer getFromNumber(Number one) {
        return one.intValue();
    }

    @Override
    public Integer getFromString(String one) {
        return Integer.parseInt(one);
    }


}

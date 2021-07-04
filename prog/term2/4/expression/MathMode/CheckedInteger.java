package expression.MathMode;

import expression.exceptions.OverflowException;

public class CheckedInteger implements MathMode<Integer> {


    @Override
    public Integer add(Integer one, Integer two) {
        return Math.addExact(one, two);
    }

    @Override
    public Integer abs(Integer one) {
        if (one == Integer.MIN_VALUE) {
            throw new OverflowException(Integer.toString(one));
        } else if (one < 0) {
            one = -one;
        }

        return one;
    }

    @Override
    public Integer negate(Integer one) {
        if (one == Integer.MIN_VALUE) {
            throw new OverflowException(Integer.toString(one));
        }
        return -one;
    }

    @Override
    public Integer multiply(Integer one, Integer two) {
        int maximum;
        if (one == 0 || two == 0) {
            return 0;
        }
        if ((one < 0 && two < 0) || (one > 0 && two > 0)) {
            maximum = Integer.MAX_VALUE;
        } else {
            maximum = Integer.MIN_VALUE;
        }
        if (two > one) {
            int temp = one;
            one = two;
            two = temp;
        }
        if ((two > 0 && two > (maximum / one) || (two < 0 && two < maximum / one))) {
            throw new OverflowException(Integer.toString(one), Integer.toString(two), "*");

        }
        return one * two;
    }

    @Override
    public Integer subtract(Integer one, Integer two) {
        int temp = one - two;

        if (((one ^ two) & (one ^ temp)) < 0) {
            throw new OverflowException(Integer.toString(one), Integer.toString(two), "-");
        }
        return one - two;
    }

    @Override
    public Integer divide(Integer one, Integer two) {
        long a = one;
        a /= two;
        if (a > Integer.MAX_VALUE) {
            throw new OverflowException(one.toString(), two.toString(), "/");
        }
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

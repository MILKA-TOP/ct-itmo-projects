package expression.MathMode;

import java.math.BigInteger;

public class CheckedBigInteger implements MathMode<BigInteger> {
    @Override
    public BigInteger add(BigInteger one, BigInteger two) {
        return one.add(two);
    }

    @Override
    public BigInteger abs(BigInteger one) {
        return one.abs();
    }

    @Override
    public BigInteger negate(BigInteger one) {
        return one.negate();
    }

    @Override
    public BigInteger multiply(BigInteger one, BigInteger two) {
        return one.multiply(two);
    }

    @Override
    public BigInteger subtract(BigInteger one, BigInteger two) {
        return one.subtract(two);
    }

    @Override
    public BigInteger divide(BigInteger one, BigInteger two) {
        return one.divide(two);
    }

    @Override
    public BigInteger getFromNumber(Number one) {
        return new BigInteger(String.valueOf(one));
    }

    @Override
    public BigInteger getFromString(String one) {
        return new BigInteger(one);
    }


}

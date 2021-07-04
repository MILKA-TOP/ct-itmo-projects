package expression.MathMode;

public interface MathMode<N> {

    N add(N one, N two);
    N abs(N one);
    N negate(N one);
    N multiply(N one, N two);
    N subtract(N one, N two);
    N divide(N one, N two);
    N getFromNumber(Number one);
    N getFromString(String one);
}

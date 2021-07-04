package expression;

public interface ExpressionFunc extends Expression, TripleExpression {
    String toString();

    boolean equals(Object obj);

    int hashCode();
}

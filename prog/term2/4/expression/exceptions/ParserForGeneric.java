package expression.exceptions;

import expression.MathMode.MathMode;
import expression.TripleGenExpression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface ParserForGeneric<N> {
    TripleGenExpression<N> parse(String expression, MathMode<N> mode) throws OverflowException, DBZException, ParsingException,VarSmallerZeroException;
}
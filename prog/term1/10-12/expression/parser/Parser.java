package expression.parser;

import expression.TripleExpression;
import expression.exceptions.DBZException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
    TripleExpression parse(String expression) throws DBZException;
}
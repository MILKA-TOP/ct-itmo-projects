package expression.generic;

import expression.MathMode.*;
import expression.TripleGenExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.ParsingException;
import expression.exceptions.UnknownModeException;

public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {

        switch (mode) {
            case "i":
                return tabulating(new CheckedInteger(), expression, x1, x2, y1, y2, z1, z2);
            case "d":
                return tabulating(new CheckedDouble(), expression, x1, x2, y1, y2, z1, z2);
            case "bi":
                return tabulating(new CheckedBigInteger(), expression, x1, x2, y1, y2, z1, z2);
            case "l":
                return tabulating(new CheckedLong(), expression, x1, x2, y1, y2, z1, z2);
            case "s":
                return tabulating(new CheckedShort(), expression, x1, x2, y1, y2, z1, z2);
            case "u":
                return tabulating(new CheckedUInteger(), expression, x1, x2, y1, y2, z1, z2);
            default:
                throw new UnknownModeException();
        }


    }

    private <N> Object[][][] tabulating(MathMode<N> mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        Object[][][] array = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        TripleGenExpression<N> tripleExpression = new ExpressionParser<N>().parse(expression, mode);


        for (int i = x1; i < x2 + 1; i++) {
            for (int j = y1; j < y2 + 1; j++) {
                for (int k = z1; k < z2 + 1; k++) {
                    try {
                        array[i - x1][j - y1][k - z1] = tripleExpression.evaluate(mode.getFromNumber(i), mode.getFromNumber(j), mode.getFromNumber(k), mode);
                    } catch (Exception E) {
                        array[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }


        return array;
    }
}

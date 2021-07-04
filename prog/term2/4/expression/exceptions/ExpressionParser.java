package expression.exceptions;

import expression.Const;
import expression.MathMode.MathMode;
import expression.TripleGenExpression;
import expression.Variable;


public class ExpressionParser<N> implements CharSource, ParserForGeneric<N> {
    MathMode<N> mode_exp;
    private String line;
    private int position;
    private TripleGenExpression<N> lastExpFunc;
    private char prevOp;
    private char lastBinOp;
    private EnumExpression prevExpEn;
    private EnumExpression nowExpEn;
    private int levelBracket = 0;

    @Override
    public TripleGenExpression<N> parse(String expression, MathMode<N> mode) throws ParsingException, OverflowException {
        position = 0;
        mode_exp = mode;
        prevExpEn = EnumExpression.START;
        line = expression;
        nulling();
        TripleGenExpression<N> temp = parseExpFunc();
        if (levelBracket < 0 || position != expression.length()) {
            throw new ParsingException(outExceptionMes("No opening parenthesis"));
        }
        return temp;
    }

    private void nulling() {
        lastBinOp = 'z';
        prevOp = 'z';            //z - стартовое значение; null - число, константа, переменная, закрывающая скобочка
        lastExpFunc = null;
    }

    private TripleGenExpression<N> parseExpFunc() throws ParsingException, OverflowException {
        skipWhiteSpaces();

        if (!hasNext() || next() == ')' && !prevExpEn.equals(EnumExpression.LEFT_BRACKET)) {
            throw new ParsingException(outExceptionMes("No last argument in binary operation"));
        }
        position--;
        char ch = next();
        StringBuilder varOrConst = new StringBuilder(Character.toString(ch));
        checkPrevParseNorm(ch);
        prevExpEn = nowExpEn;
        if (ch == '(') {
            parseExpInBrack();
        } else if (Character.isDigit(ch)) {
            parseConst(varOrConst);
        } else if (Character.isAlphabetic(ch)) {
            parseVarOrSomeOperation(varOrConst);
        } else if (ch == '-' && prevOp != ' ') {
            parseUnaryMines();
        } else {
            if (ch == ')') {
                checkPrevParseNorm(ch);
                return lastExpFunc;
            }
            parseBinOp(ch);
            if (isGoUpBrack(lastBinOp, true)) {
                return lastExpFunc;
            }
        }
        if (varOrConst.toString().equals("abs") || varOrConst.toString().equals("sqrt")) {

            if (varOrConst.toString().equals("abs")) {
                parseSomeOperation('a');
            } else {
                parseSomeOperation('s');
            }

        }

        if (isGoUpBrack(prevOp, false)) {
            return lastExpFunc;
        }

        if (hasNext()) {
            ch = now();
            if (ch == ')') {
                return lastExpFunc;
            }
            lastExpFunc = parseExpFunc();
        }
        return lastExpFunc;
    }

    private void parseExpInBrack() throws ParsingException, OverflowException {
        char tempPrevOp = prevOp;
        char tempLastBinOp = lastBinOp;
        nulling();
        levelBracket++;
        parseExpFunc();
        if (!hasNext()) {
            throw new ParsingException(outExceptionMes("No closing parenthesis"));
        }
        next();
        prevOp = tempPrevOp;
        lastBinOp = tempLastBinOp;
        skipWhiteSpaces();
    }

    private void parseBinOp(char ch) throws ParsingException, OverflowException {
        char temp = lastBinOp;
        prevOp = ch;
        lastBinOp = ch;
        switch (ch) {
            case ('*'):
                lastExpFunc = new CheckedMultiply<N>(lastExpFunc, parseExpFunc());
                break;
            case ('/'):
                lastExpFunc = new CheckedDivide<N>(lastExpFunc, parseExpFunc());
                break;
            case ('+'):
                lastExpFunc = new CheckedAdd<N>(lastExpFunc, parseExpFunc());
                break;
            case ('-'):
                lastExpFunc = new CheckedSubtract<N>(lastExpFunc, parseExpFunc());
                break;
            default:
                throw new ParsingException();
        }

        lastBinOp = temp;
        skipWhiteSpaces();
    }

    public void parseSomeOperation(char kindOfOperation) throws ParsingException, OverflowException {
        char temp = prevOp;
        prevOp = kindOfOperation;
        if (!hasNext()) {
            throw new ParsingException(outExceptionMes("Invalid variable"));
        }

        if (kindOfOperation == 'a') {
            lastExpFunc = new CheckedAbs<N>(parseExpFunc());
        }

        prevOp = temp;
        skipWhiteSpaces();
    }

    private void parseUnaryMines() throws ParsingException, OverflowException {
        char temp = prevOp;
        prevOp = '~';
        char ch;
        if (hasNext()) {
            ch = next();
        } else {
            throw new ParsingException(outExceptionMes("Invalid variable"));
        }
        if (Character.isDigit(ch)) {
            parseConst(new StringBuilder("-" + ch));
            prevExpEn = EnumExpression.NUMB;
        } else {
            position--;
            lastExpFunc = new CheckedNegate<N>(parseExpFunc());
        }
        prevOp = temp;
        skipWhiteSpaces();
    }


    private void parseVarOrSomeOperation(StringBuilder sb) throws ParsingException {
        while (hasNext() && Character.isAlphabetic(now()) && !Character.isWhitespace(now())) {
            sb.append(next());
        }
        if (!sb.toString().equals("x") && !sb.toString().equals("y") && !sb.toString().equals("z")
                && !sb.toString().equals("abs") && !sb.toString().equals("sqrt")) {
            throw new ParsingException(outExceptionMes("Invalid variable"));
        }
        lastExpFunc = new Variable<N>(sb.toString());
        skipWhiteSpaces();
    }

    private void parseConst(StringBuilder sb) throws OverflowException {
        while (hasNext() && Character.isDigit(now())) {
            sb.append(next());
        }
        try {
            lastExpFunc = new Const<N>(mode_exp.getFromString(sb.toString()));
        } catch (NumberFormatException exception) {
            throw new OverflowException(sb.toString());
        }
        skipWhiteSpaces();
    }

    private boolean isGoUpBrack(char checkFunc, boolean isItOperation) throws ParsingException {
        prevOp = ' ';


        if (!hasNext()) {
            return true;
        } else {
            char ch = now();
            if (!isItOperation) {
                checkPrevParseNorm(ch);
//                prevExpEn = nowExpEn;
            }

            return priorOfFunc(checkFunc) >= priorOfFunc(ch)
                    || (lastBinOp == 'a' && checkFunc != 's')
                    || (lastBinOp == 's' && checkFunc != 'a');
        }
    }

    private String outExceptionMes(String mes) {
        StringBuilder out = new StringBuilder(mes + "\n" + line + "\n");
        for (int i = 0; i < position - 1; i++) {
            out.append(" ");
        }
        out.append("^");
        return out.toString();
    }

    private void skipWhiteSpaces() {
        while (hasNext() && Character.isWhitespace(now())) {
            next();
        }
    }

    private int priorOfFunc(char ch) {
        switch (ch) {
            case ('a'):
            case ('s'):
            case ('~'):
                return 6;
            case ('*'):
            case ('/'):
                return 5;
            case ('+'):
            case ('-'):
                return 4;
            case ('&'):
                return 3;
            case ('^'):
                return 2;
            case ('|'):
                return 1;
            default:
                return 0;
        }
    }


    public void checkPrevParseNorm(char ch) throws ParsingException {
        if (ch == '+' || ch == '-' || ch == '/' || ch == '*') {
            nowExpEn = EnumExpression.OPERATION;
        } else if (Character.isDigit(ch) || ch == 'x' || ch == 'y' || ch == 'z' || ch == 'a' || ch == 's') {
            nowExpEn = EnumExpression.NUMB;
            if (ch == 's' || ch == 'a') {
                nowExpEn = EnumExpression.ABS_SQRT_OP;
            }
        } else if (ch == '(') {
            nowExpEn = EnumExpression.LEFT_BRACKET;
        } else if (ch == ')') {
            nowExpEn = EnumExpression.RIGHT_BRACKET;
        } else {
            throw new ParsingException(outExceptionMes("Input unknown character"));
        }

        if ((prevExpEn.equals(EnumExpression.START) || prevExpEn.equals(EnumExpression.LEFT_BRACKET)) && nowExpEn.equals(EnumExpression.OPERATION) && ch != '-') {
            throw new ParsingException(outExceptionMes("No first argument in binary operation"));
        } else if (prevExpEn.equals(nowExpEn) && nowExpEn.equals(EnumExpression.NUMB)) {
            throw new ParsingException(outExceptionMes("Spaces in numbers"));
        } else if (levelBracket < 0) {
            throw new ParsingException(outExceptionMes("No opening parenthesis"));
        } else if (nowExpEn.equals(EnumExpression.RIGHT_BRACKET) && prevExpEn.equals(EnumExpression.LEFT_BRACKET)) {
            throw new ParsingException(outExceptionMes("No arguments in ()"));
        } else if (prevExpEn.equals(EnumExpression.OPERATION) && prevExpEn.equals(nowExpEn) && ch != '-' && hasNext()) {
            throw new ParsingException(outExceptionMes("No middle argument in binary operation"));
        }

    }

    @Override
    public boolean hasNext() {
        return position < line.length();
    }

    @Override
    public char next() {
        return line.charAt(position++);
    }

    @Override
    public char now() {
        return line.charAt(position);
    }

}

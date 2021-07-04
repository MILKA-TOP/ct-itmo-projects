package expression.parser;

import expression.Const;
import expression.Mines;
import expression.TripleExpression;
import expression.Variable;
import expression.exceptions.CharSource;
import expression.exceptions.Parser;
import expression.exceptions.*;


public class ExpressionParser implements CharSource, Parser {
    private String line;
    private int position;
    private TripleExpression lastExpFunc;
    private char prevOp;
    private char lastBinOp;

    @Override
    public TripleExpression parse(String expression) {
        position = 0;
        line = expression;
        nulling();
        return parseExpFunc();
    }

    private void nulling() {
        lastBinOp = 'z';
        prevOp = 'z';            //z - стартовое значение; null - число, константа, переменная, закрывающая скобочка
        lastExpFunc = null;
    }

    private TripleExpression parseExpFunc() {
        skipWhiteSpaces();
        char ch = next();
        StringBuilder varOrConst = new StringBuilder(Character.toString(ch));
        if (ch == '(') {
            parseExpInBrack();
        } else if (Character.isDigit(ch)) {
            parseConst(varOrConst);
        } else if (Character.isAlphabetic(ch)) {
            parseVar(varOrConst);
        } else if (ch == '-' && prevOp != ' ') {
            parseUnaryMines();
        } else {
            parseBinOp(ch);
            if (isGoUpBrack(lastBinOp)) {
                return lastExpFunc;
            }
        }

        if (isGoUpBrack(prevOp)) {
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

    private void parseExpInBrack() {
        char tempPrevOp = prevOp;
        char tempLastBinOp = lastBinOp;
        nulling();
        parseExpFunc();
        next();
        prevOp = tempPrevOp;
        lastBinOp = tempLastBinOp;
        skipWhiteSpaces();
    }

    private void parseBinOp(char ch) {
        char temp = lastBinOp;
        prevOp = ch;
        lastBinOp = ch;

        switch (ch) {
            case ('*'):
                lastExpFunc = new CheckedMultiply(lastExpFunc, parseExpFunc());
                break;
            case ('/'):
                lastExpFunc = new CheckedDivide(lastExpFunc, parseExpFunc());
                break;
            case ('+'):
                lastExpFunc = new CheckedAdd(lastExpFunc, parseExpFunc());
                break;
            case ('-'):
                lastExpFunc = new CheckedSubtract(lastExpFunc, parseExpFunc());
                break;
            default:
                lastExpFunc = null;
        }

        lastBinOp = temp;
        skipWhiteSpaces();
    }

    private void parseUnaryMines() {
        char temp = prevOp;
        prevOp = '~';
        char ch = next();
        if (Character.isDigit(ch)) {
            parseConst(new StringBuilder("-" + ch));
        } else {
            position--;
            lastExpFunc = new Mines(parseExpFunc());
        }
        prevOp = temp;
        skipWhiteSpaces();
    }

    private void parseVar(StringBuilder sb) {
        while (hasNext() && Character.isAlphabetic(now())) {
            sb.append(next());
        }
        lastExpFunc = new Variable(sb.toString());
        skipWhiteSpaces();
    }

    private void parseConst(StringBuilder sb) {
        while (hasNext() && Character.isDigit(now())) {
            sb.append(next());
        }
        lastExpFunc = new Const(Integer.parseInt(sb.toString()));
        skipWhiteSpaces();
    }

    private boolean isGoUpBrack(char checkFunc) {
        prevOp = ' ';
        if (!hasNext()) {
            return true;
        } else {
            char ch = now();
            if (priorOfFunc(checkFunc) >= priorOfFunc(ch)) {
                return true;
            }
            return false;
        }
    }

    private void skipWhiteSpaces() {
        while (hasNext() && Character.isWhitespace(now())) {
            next();
        }
    }

    private int priorOfFunc(char ch) {

        switch (ch) {
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

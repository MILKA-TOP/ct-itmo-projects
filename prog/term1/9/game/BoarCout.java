package game;

import java.util.Map;

public class BoarCout implements Position {
    private static final Map<Cell, Character> symbolsOfCout = Map.of(Cell.X, 'X', Cell.O, 'O', Cell.E, '.');
    private static int m, n;
    private int k, countsZero;
    private Cell[][] board;
    private Cell turn = Cell.X;

    public BoarCout(int m, int n, int k) {
        this.m = m;
        this.n = n;
        this.k = k;
        cleanBoard(m, n, k);
    }

    public static int getM() {
        return m;
    }

    public static int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public void cleanBoard(int m, int n, int k) {
        countsZero = m * n;
        this.board = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = Cell.E;
            }
        }
        turn = Cell.X;
    }


    @Override
    public String toString() {
        StringBuilder returnBoardStringBuilder = new StringBuilder();
        StringBuilder up = new StringBuilder();
        up.append(" ");
        for (int i = 1; i <= m; i++) {
            up.append(i);
        }
        up.append("\n");
        returnBoardStringBuilder.append(up);
        for (int i = 0; i < n; i++) {
            returnBoardStringBuilder.append(i + 1);
            for (int j = 0; j < m; j++) {
                returnBoardStringBuilder.append(symbolsOfCout.get(board[i][j]));
            }
            returnBoardStringBuilder.append("\n");
        }
        return returnBoardStringBuilder.toString();
    }

    public Cell getTurn() {
        return turn;
    }

    @Override
    public Cell get(final int row, final int col) {
        return board[row][col];
    }

    @Override
    public boolean isValid(final Move move) {
        int a = move.getRow(), b = getN(), c = move.getCol(), d = getM();
        if (0 <= move.getRow() && move.getRow() < getN()
                && 0 <= move.getCol() && move.getCol() < getM()) {
            return get(move.getRow(), move.getCol()) == Cell.E
                    && move.getValue() == turn;
        }
        return false;
    }

    public Results makeMove(Move move) {
        if (!isValid(move)) {
            return Results.LOSE;
        }
        board[move.getRow()][move.getCol()] = move.getValue();
        countsZero--;
        Results tempResults;


        if (isInLineRow(move.getRow(), getM(), true)
                || isInLineRow(move.getCol(), getN(), false)
                || inDiag(true, move)
                || inDiag(false, move)) {
            tempResults = Results.WIN;
        } else if (countsZero == 0) {
            tempResults = Results.DRAW;
        } else {
            tempResults = Results.UNKNOWN;
        }

        if (turn == Cell.X) {
            turn = Cell.O;
        } else {
            turn = Cell.X;
        }

        return tempResults;

    }

    private boolean isInLineRow(int constant, final int size, boolean isItCheckLine) {
        for (int i = 0; i < size; i++) {
            int countsTogether = 0;
            while (i < size) {
                if (isItCheckLine && get(constant, i) == turn) {
                    countsTogether++;
                } else if (!isItCheckLine && get(i, constant) == turn) {
                    countsTogether++;
                } else {
                    break;
                }
                i++;
            }
            if (countsTogether >= k) {
                return true;
            }
        }
        return false;
    }

    private boolean inDiag(boolean isItLeftToRight, Move move) {
        int rowId = move.getRow(), colId = move.getCol(), nowIdRow, nowIdCol, minus;
        if (isItLeftToRight) {
            minus = Math.min(rowId, colId);
            nowIdCol = colId - minus;
        } else {
            minus = Math.min(rowId, getM() - colId - 1);
            nowIdCol = colId + minus;
        }
        nowIdRow = rowId - minus;

        while (nowIdRow < getN() && checkBorder(isItLeftToRight, nowIdCol)) {
            int countsTogether = 0;
            while (nowIdRow < getN() && checkBorder(isItLeftToRight, nowIdCol)) {
                if (get(nowIdRow, nowIdCol) == turn) {
                    countsTogether++;
                    nowIdRow++;
                    int temp = isItLeftToRight ? nowIdCol++ : nowIdCol--;

                } else {
                    break;
                }
            }
            if (countsTogether >= k) {
                return true;
            }
            int temp = isItLeftToRight ? nowIdCol++ : nowIdCol--;
            nowIdRow++;
        }


        return false;
    }


    private boolean checkBorder(boolean isItLeftToRight, int nowIdCol) {
        if (isItLeftToRight) {
            return nowIdCol < getM();
        } else {
            return nowIdCol > -1;
        }
    }

}

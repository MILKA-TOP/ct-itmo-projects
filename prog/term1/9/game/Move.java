package game;

public class Move {
    private final int row; //строка
    private final int col; //столбец
    private final Cell value; //значение в клетке

    public Move(final int row, int col, Cell value) {
        this.col = col;
        this.row = row;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public Cell getValue() {
        return value;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "{row=" + (row + 1) + ", column=" + (col + 1) + ", value=" + value + "}";
    }


}

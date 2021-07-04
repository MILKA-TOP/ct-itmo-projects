package game;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner sc;

    public HumanPlayer(final Scanner sc) {
        this.sc = sc;
    }

    public HumanPlayer() {
        this(new Scanner(System.in));
    }

    @Override
    public Move makeMove(final Position position, final Cell cell) {
        String tempLine1, tempLine2;
        Scanner lineScanner;
        String[] inputArr;
        int countsInLine;
        while (true) {
            System.out.println("Position");
            System.out.println(position.toString());
            System.out.println(cell + "'s move");
            do {
                System.out.println("Enter row and column");
                countsInLine = 0;
                inputArr = new String[2];
                lineScanner = new Scanner(sc.nextLine());
                while (lineScanner.hasNext() && countsInLine < 2) {
                    inputArr[countsInLine] = lineScanner.next();
                    countsInLine++;
                }
                tempLine1 = inputArr[0];
                tempLine2 = inputArr[1];
            } while (lineScanner.hasNext() || !Game.isItInt(tempLine1) || !Game.isItInt(tempLine2));
            int row = Integer.parseInt(tempLine1) - 1;
            int col = Integer.parseInt(tempLine2) - 1;
            final Move move = new Move(row, col, cell);
            if (position.isValid(move)) {
                return move;
            }
            System.out.println("Invalid move: row " + (row + 1) + ", column " + (col + 1));
        }
    }
}


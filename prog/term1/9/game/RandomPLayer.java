package game;

import java.util.Random;

public class RandomPLayer implements Player {
    private final Random rnd = new Random();

    @Override
    public Move makeMove(final Position position, Cell cell) {
        Move move;
        do {
            move = new Move(rnd.nextInt(BoarCout.getN()), rnd.nextInt(BoarCout.getM()), cell);
        } while (!position.isValid(move));
        return move;
    }
}

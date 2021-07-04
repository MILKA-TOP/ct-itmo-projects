package game;

public class SequentialPlayer implements Player {
    @Override
    public Move makeMove(final Position position, final Cell cell) {

        for (int r = 0; r < BoarCout.getN(); r++) {
            for (int c = 0; c < BoarCout.getM(); c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}

package game;

public class Main {

    public static void main(String[] args) {
        final Game game = new Game(new RandomPLayer(), new RandomPLayer(), false);
        game.play();
    }
}

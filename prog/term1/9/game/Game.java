package game;

import java.util.Scanner;

public class Game {
    private final Player player1;
    private final Player player2;
    private final boolean enableLogging;
    private int countPlayers, countCircles;
    private Results[][] resultCell = new Results[countPlayers][countPlayers];
    private int[] scores = new int[countPlayers];
    private boolean isItTourn = true;

    public Game(final Player player1, final Player player2, final boolean enableLogging) {
        this.player1 = player1;
        this.player2 = player2;
        this.enableLogging = enableLogging;
    }

    public static boolean isItInt(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid input - " + line);
            return false;
        }
    }

    private void regularGame(BoarCout boarCout) {
        int result;
        boarCout.cleanBoard(boarCout.getM(), boarCout.getN(), boarCout.getK());
        while (true) {
            result = makeMove(boarCout, 1, this.player1);
            if (result >= 0) {
                break;
            }
            result = makeMove(boarCout, 2, this.player2);
            if (result >= 0) {
                break;
            }
        }
        if (result == 0) {
            System.out.println("Draw");

        } else {
            System.out.println("Game result: player " + result + " won");
        }
    }

    private void circleGame(BoarCout boarCout) {
        int result;
        this.resultCell = new Results[countPlayers][countPlayers];
        this.scores = new int[countPlayers];
        for (int cicle = 0; cicle < countCircles; cicle++) {
            for (int i = 0; i < countPlayers; i++) {
                for (int j = i + 1; j < countPlayers; j++) {
                    boarCout.cleanBoard(boarCout.getM(), boarCout.getN(), boarCout.getK());
                    int first = i, second = j;
                    if (cicle % 2 != 0) {
                        int temp = first;
                        first = second;
                        second = temp;
                    }
                    System.out.println("Now will play --Player " + Integer.toString(first + 1) + "-- and --Player " + Integer.toString(second + 1) + "--");
                    while (true) {
                        result = makeMove(boarCout, first + 1, this.player1);
                        if (result >= 0) {
                            break;
                        }
                        result = makeMove(boarCout, second + 1, this.player2);
                        if (result >= 0) {
                            break;
                        }
                    }
                    if (result == 0) {
                        System.out.println("Draw");
                        this.resultCell[i][j] = Results.DRAW;
                        this.resultCell[j][i] = Results.DRAW;
                        scores[i] += 1;
                        scores[j] += 1;
                    } else {
                        System.out.println("Game result: player " + result + " won");
                        if (result == first + 1) {
                            this.resultCell[first][second] = Results.WIN;
                            scores[first] += 3;
                            this.resultCell[second][first] = Results.LOSE;
                        } else {
                            this.resultCell[second][first] = Results.LOSE;
                            this.resultCell[first][second] = Results.WIN;
                            scores[first] += 3;
                        }
                    }

                }
            }
            System.out.println("\nRESULTS AFTER CIRCLE NUMBER " + Integer.toString(cicle + 1));
            for (int i = 0; i < countPlayers; i++) {
                System.out.println("PLAYER " + Integer.toString(i + 1) + "===" + scores[i]);
            }
            System.out.println("\n=========");

            for (int i = 0; i < countPlayers; i++) {
                for (int j = 0; j < countPlayers; j++) {
                    this.resultCell[i][j] = null;
                }
            }
        }

        int[] out = findWinner(scores);
        System.out.println("PLAYER " + (out[0] + 1) + " WIN THIS GAME WITH " + out[1] + " POINTS");
    }

    private int[] creatingBoard(Scanner sc) {
        int countsInLine;
        String mLine, nLine, kLine;
        String[] inputArr;
        Scanner lineScanner;
        do {
            countsInLine = 0;
            inputArr = new String[3];
            System.out.println("Please, input correct m, n, k (number), count of circles (с) and count of players");
            System.out.println("We need 'k' less than 'm' or 'n'.");
            lineScanner = new Scanner(sc.nextLine());
            while (lineScanner.hasNext() && countsInLine < 3) {
                inputArr[countsInLine] = lineScanner.next();
                countsInLine++;
            }
            mLine = inputArr[0];
            nLine = inputArr[1];
            kLine = inputArr[2];
        } while (lineScanner.hasNext() || !checkNumBoard(mLine, nLine, kLine));
        return new int[]{Integer.parseInt(mLine), Integer.parseInt(nLine), Integer.parseInt(kLine)};
    }

    private boolean checkTourney(Scanner sc) {
        String cLine, countPLayerLine;
        int countsInLine;
        String[] inputArr;
        Scanner lineScanner;
        System.out.println("If you don't want to play a tourney, input '0 0', else input counts of circles and count of players: ");
        do {
            countsInLine = 0;
            inputArr = new String[2];
            System.out.println("Input correct counts");
            lineScanner = new Scanner(sc.nextLine());
            while (lineScanner.hasNext() && countsInLine < 2) {
                inputArr[countsInLine] = lineScanner.next();
                countsInLine++;
            }
            cLine = inputArr[0];
            countPLayerLine = inputArr[1];
        } while (lineScanner.hasNext() || !checkNumTourney(cLine, countPLayerLine));
        this.countPlayers = Integer.parseInt(countPLayerLine);
        this.countCircles = Integer.parseInt(cLine);
        if (cLine.equals("0") && countPLayerLine.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the game MNK");
        int[] tempMNK = creatingBoard(sc);
        int m = tempMNK[0], n = tempMNK[1], k = tempMNK[2];
        BoarCout boarCout = new BoarCout(m, n, k);
        this.isItTourn = checkTourney(sc);

        if (this.isItTourn) {
            circleGame(boarCout);
        } else {
            regularGame(boarCout);
        }


    }


    private boolean checkNumBoard(String mLine, String nLine, String kLine) {
        if (isItInt(mLine) && isItInt(nLine) && isItInt(kLine)
                && Integer.parseInt(mLine) > 0 && Integer.parseInt(nLine) > 0 && Integer.parseInt(kLine) > 0) {
            if (Integer.parseInt(kLine) <= Math.max(Integer.parseInt(mLine), Integer.parseInt(nLine))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkNumTourney(String cLine, String countPLayerLine) {
        if (isItInt(cLine) && isItInt(countPLayerLine)
                && ((Integer.parseInt(cLine) == 0 && Integer.parseInt(countPLayerLine) == 0)
                || (Integer.parseInt(countPLayerLine) > 1 && Integer.parseInt(cLine) > 0))) {
            return true;
        } else {
            return false;
        }
    }

    private int[] findWinner(int[] scores) {
        int maxScores = 0;
        int index = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] >= maxScores) {
                maxScores = scores[i];
                index = i;
            }
        }
        int[] out = new int[]{index, maxScores};
        return out;
    }

    private int makeMove(final BoarCout boarCout, final int playerNum, final Player player) {
        final Move move = player.makeMove(boarCout, boarCout.getTurn());
        final Results results = boarCout.makeMove(move);
        log("Player " + playerNum + " move: " + move);
        log("Position:\n" + boarCout);
        if (results == Results.WIN) {
            log("Player " + playerNum + " won");
            return playerNum;
        } else if (results == Results.DRAW) {
//            log("Draw");
            return 0;
        } else if (results == Results.LOSE) {
            log("Player " + playerNum + " lose");
            return 3 - playerNum;
        } else if (results == Results.UNKNOWN) {
            return -1;
        } else {
            throw new AssertionError("Invalid Input");
        }
    }

    private void log(final String message) {
        if (enableLogging) {
            System.out.println(message);
        }
    }


}

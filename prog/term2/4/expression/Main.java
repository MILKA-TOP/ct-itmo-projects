package expression;


import expression.generic.GenericTabulator;

public class Main {

    public static void main(String[] args) throws Exception {
        String mode = args[0];
        String expression = args[1];
        int a = -2;
        int b = 2;
        Object[][][] array = new GenericTabulator().tabulate(mode, expression, a, b, a, b, a, b);

        for (int i = a; i <= b; i++) {
            for (int j = a; j <= b; j++) {
                for (int k = a; k <= b; k++) {
                    System.out.println("x = " + i + "; y = " + j + "; z = " + k + "; OUT: " + array[i + 2][j + 2][k + 2]);
                }
            }
        }
    }
}

package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static long[][] dSolve(int n) {
        long[][] d = new long[2 * n + 1][n + 1];
        d[0][0] = 1;
        for (int i = 1; i < n + 1; i++) {
            d[0][i] = 0;
        }
        for (int i = 0; i < n * 2; i++)
            for (int j = 0; j < n + 1; j++) {
                if (j + 1 <= n)
                    d[i + 1][j + 1] = d[i][j] + d[i + 1][j + 1];
                if (j > 0)
                    d[i + 1][j - 1] = d[i][j] + d[i + 1][j - 1];
            }
        return d;

    }

    public static String outProg(long[][] d, int n, long k) {
        int de = 0;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 2 * n; i++) {
            if (d[2 * n - (i + 1)][de + 1] >= k) {
                out.append("(");
                de++;
            } else {
                k -= d[2 * n - i - 1][de + 1];
                out.append(")");
                de--;
            }
        }
        return out.toString();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("num2brackets.in"))) {
            try (FileWriter wr = new FileWriter("num2brackets.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                int n = sc.nextInt();
                long k = sc.nextLong();
//                int n = 4, k = 10;

                if (k == 0) {
                    for (int i = 0; i < n; i++) {
                        wr.write("(");
                    }
                    for (int i = 0; i < n; i++) {
                        wr.write(")");
                    }
                } else {
                    long[][] d = dSolve(n);
                    wr.write(outProg(d, n, k + 1) + "\n");
                }


                /////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
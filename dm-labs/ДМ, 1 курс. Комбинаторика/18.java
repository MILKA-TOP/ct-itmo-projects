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
                    d[i + 1][j + 1] += d[i][j];
                if (j > 0)
                    d[i + 1][j - 1] += d[i][j];
            }
        return d;

    }

    public static long outProg(long[][] d, char[] arr, int n) {
        long a = 0, de = 0;
        for (int i = 0; i < 2 * n; i++) {
            if (arr[i] == '(') {
                de++;
            } else {
                a += d[2 * n - i - 1][(int)de + 1];
                de--;
            }
        }
        return a;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("brackets2num.in"))) {
            try (FileWriter wr = new FileWriter("brackets2num.out")) {
                /////////////////////////////////////////////////////////////////////////////////

                char[] arr = sc.next().toCharArray();
                int n = arr.length / 2;
                long[][] d = dSolve(n);
                int counts = 0;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] == '(') {
                        counts++;
                    } else {
                        break;
                    }
                }
                if (counts == n) {
                    wr.write("0");
                } else {
                    wr.write(Long.toString(outProg(d, arr, n)));
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
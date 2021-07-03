package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static long generate(int n, int k, int[] a, long[][] c) {
        long outNum = 0;
        for (int i = 1; i <= k; i++) {
            for (int j = a[i - 1] + 1; j < a[i]; j++) {
                outNum += c[n - j][k - i];
            }
        }
        return outNum;
    }


    public static long[][] pascalTer(long n) {
        long[][] pasc;
        if (n >= 2) {
            pasc = new long[(int) n][];
            pasc[0] = new long[1];
            pasc[1] = new long[2];
            pasc[0][0] = 1;
            pasc[1][0] = 1;
            pasc[1][1] = 1;
            for (int i = 2; i < n; i++) {
                pasc[i] = new long[i + 1];
                pasc[i][0] = 1;
                pasc[i][i] = 1;
                for (int j = 1; j < i; j++) {
                    pasc[i][j] = pasc[i - 1][j - 1] + pasc[i - 1][j];
                }
            }
        } else {
            pasc = new long[2][];
            pasc[0] = new long[]{1};
            pasc[1] = new long[]{1, 1};
        }
        return pasc;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("choose2num.in"))) {
            try (FileWriter wr = new FileWriter("choose2num.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                int n = sc.nextInt(), k = sc.nextInt();
                int[] a = new int[k + 1];
                for (int i = 0; i < k; i++) {
                    a[i + 1] = sc.nextInt();
                }

                long[][] c = pascalTer(n);

                long out = generate(n, k, a, c);
                wr.write(Long.toString(out));


                /////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}

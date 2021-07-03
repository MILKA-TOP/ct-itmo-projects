package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private long[] facrotialsN;

    public static long sochet(long up, long down, long[] factorialsN) {
        return factorialsN[(int) up] / (factorialsN[(int) down] * factorialsN[(int) (up - down)]);
    }

    public static long[] generate(int n, int k, long m, long[] a, long[][] c) {
        long numberOfChange = 1;
        int addId = k - 1;
        while (k > 0) {
            if (m < c[n - 1][k - 1]) {
                a[addId--] = numberOfChange;
                k--;
            } else {
                m -= c[n - 1][k - 1];
            }
            n--;
            numberOfChange++;
        }
        return a;
    }

    public static long[][] cSochet(long n, long k, long[] facrotialsN) {
        long[][] c = new long[(int) (n + 1)][(int) (n + 1)];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                c[i][j] = sochet(i, j, facrotialsN);
            }
        }
        return c;
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
        try (Scanner sc = new Scanner(new File("num2choose.in"))) {
            try (FileWriter wr = new FileWriter("num2choose.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                int n = sc.nextInt(), k = sc.nextInt();
                long m = sc.nextLong();
                long[] a = new long[k];
/*                long[] facrotialsN = new long[(int) (n + 1)];
                facrotialsN[0] = 1;
                for (int i = 1; i <= n; i++) {
                    facrotialsN[i] = facrotialsN[i - 1] * i;
                }*/
//                long[][] c = cSochet(n, k, facrotialsN);
                long[][] c = pascalTer(n);

                a = generate(n, k, m, a, c);
                for (int i = a.length - 1; i > -1; i--) {
                    wr.write(a[i] + " ");
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

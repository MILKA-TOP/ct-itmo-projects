package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static long[][] dSolve(int n) {
        long[][] d = new long[n + 1][n + 1];
        for (int i = 0; i < n + 1; i++) {
            d[i][i] = 1;
        }
        for (int i = 1; i < n + 1; i++) {
            for (int j = i - 1; j > 0; j--) {
                d[i][j] = d[i][j + 1] + d[i - j][j];
            }
        }
        return d;

    }

    public static ArrayList<Integer> outProg(long[][] d, int n, int k) {
        ArrayList<Integer> arr = new ArrayList<>();
        int outInt = k, lastInt = 0, sumOfElements = 0;
        while (outInt >= 0 && sumOfElements < n) {
            for (int j = lastInt; j <= n + 1; j++) {
                if (n - sumOfElements - j >= 0) {
                    if (outInt - d[n - sumOfElements - j][j] >= 0) {
                        outInt -= d[n - sumOfElements - j][j];
                    } else {
                        arr.add(j);
                        break;
                    }
                } else {
                    arr.add(j - 1);
                    break;
                }
            }
            lastInt = arr.get(arr.size() - 1);
            sumOfElements += lastInt;
        }
        return arr;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("num2part.in"))) {
            try (FileWriter wr = new FileWriter("num2part.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                int n = sc.nextInt();
                int k = sc.nextInt();

                long[][] d = dSolve(n);
                List<Integer> secMinArr = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    secMinArr.add(1);
                }
                List<Integer> arr = outProg(d, n, k);
                for (int i = 0; i < arr.size() - 1; i++) {
                    wr.write(Integer.toString(arr.get(i)) + "+");
                }
                wr.write(Integer.toString(arr.get(arr.size() - 1)));

                /////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
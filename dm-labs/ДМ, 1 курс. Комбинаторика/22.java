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
            for (int j = i - 1; j > -1 ; j--) {
                d[i][j] = d[i][j + 1] + d[i - j][j];
            }
        }
        return d;

    }

    public static int outProg(List<Integer> arr, long[][] d, int n) {
        int outInt = 0, lastInt = 0, sumOfElements = 0;
        for (int i = 0; i < arr.size(); i++) {
            for (int j = lastInt; j < arr.get(i); j++) {
                outInt += d[n - sumOfElements - j][j];
            }
            sumOfElements += arr.get(i);
            lastInt = arr.get(i);
        }
        return outInt;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("part2num.in"))) {
            try (FileWriter wr = new FileWriter("part2num.out")) {
                /////////////////////////////////////////////////////////////////////////////////

                String line = sc.next();
                line = line.replaceAll("\\+", " ");
                int n = 0;
                Scanner scan = new Scanner(line);
                List<Integer> arr = new ArrayList<>();
                while (scan.hasNext()) {
                    int tempInt = scan.nextInt();
                    n += tempInt;
                    arr.add(tempInt);
                }

                long[][] d = dSolve(n);
                List<Integer> secMinArr = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    secMinArr.add(1);
                }
                int minus = outProg(secMinArr, d, n);
                wr.write(Integer.toString(outProg(arr, d, n) - minus));




                /////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
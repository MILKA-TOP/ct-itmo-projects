package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void outArr(int[] arr, FileWriter wr) throws IOException {
        for (int i = 0; i < arr.length; i++) {
            wr.write(arr[i] + " ");
        }
    }

    public static int[] reverseArr(int[] arr, int firstIndex, int secondIndex) {
        int[] tempArr = arr.clone();
        for (int i = 0; i < secondIndex - firstIndex + 1; i++) {
            arr[firstIndex + i] = tempArr[secondIndex - i];
        }
        return arr;
    }

    public static int[] previousNumber(int[] arr, int n, int k) {

        arr[0] = 0;
        for (int i = k; i > 0; i--) {
            if ((arr[i] - arr[i - 1]) > 1) {
                arr[i]--;
                int count = Math.max(arr[i] + 1, n - k + i + 1);
                for (int j = i + 1; j <= k; j++) {
                    arr[j] = count++;
                }
                return arr;
            }
        }

        return null;
    }

    public static int[] nextNumber(int[] arr, int n, int k) {
        int[] tempArr = new int[k + 1];
        for (int i = 0; i < k; i++) {
            tempArr[i] = arr[i];
        }
        tempArr[k] = n + 1;
        int i = k - 1;
        while (i >= 0 && (tempArr[i + 1] - tempArr[i]) < 2) {
            i--;
        }
        if (i >= 0) {
            tempArr[i] += 1;
            for (int j = i + 1; j < k; j++) {
                tempArr[j] = tempArr[j - 1] + 1;
            }
            for (int j = 0; j < k; j++) {
                arr[j] = tempArr[j];
            }
            return arr;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("nextchoose.in"))) {
            try (FileWriter wr = new FileWriter("nextchoose.out")) {
                /////////////////////////////////////////////////////////////////////////////////

                int n = sc.nextInt(), k = sc.nextInt();
                int[] arr = new int[k];
                for (int i = 0; i < k; i++) {
                    arr[i] = sc.nextInt();
                }

                if (n == 1) {
                    wr.write("-1");
                } else {
//                int[] prev = previousNumber(arr.clone(), n, k);

                    int[] next = nextNumber(arr.clone(), n, k);

                    if (next == null) {
                        wr.write("-1");
                    } else {
                        for (int i = 0; i < k; i++) {
                            wr.write(next[i] + " ");
                        }
                    }
                }
            }

            /////////////////////////////////////////////////////////////////////////////////
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

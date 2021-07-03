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

    public static int[] previousNumber(int[] arr, int n) {
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                int maximum = i + 1;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[i] && arr[j] > arr[maximum]) {
                        maximum = j;
                    }
                }


                int temp = arr[i];
                arr[i] = arr[maximum];
                arr[maximum] = temp;

                arr = reverseArr(arr, i + 1, n - 1);

                return arr;
            }
        }
        return null;
    }

    public static int[] nextNumber(int[] arr, int n) {
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] < arr[i + 1]) {
                int minimum = i + 1;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] > arr[i] && arr[j] < arr[minimum]) {
                        minimum = j;
                    }
                }


                int temp = arr[i];
                arr[i] = arr[minimum];
                arr[minimum] = temp;

                arr = reverseArr(arr, i + 1, n - 1);

                return arr;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("nextperm.in"))) {
            try (FileWriter wr = new FileWriter("nextperm.out")) {
                /////////////////////////////////////////////////////////////////////////////////

                int n = sc.nextInt();
                int[] arr = new int[n];
                for (int i = 0; i < n; i++) {
                    arr[i] = sc.nextInt();
                }

                if (n != 1) {
                    int[] prev = previousNumber(arr.clone(), n);

                    int[] next = nextNumber(arr.clone(), n);

                    if (prev == null) {
                        for (int i = 0; i < n; i++) {
                            wr.write("0 ");
                        }
                        wr.write("\n");
                        outArr(next, wr);

                    } else if (next == null) {
                        outArr(prev, wr);
                        wr.write("\n");
                        for (int i = 0; i < n; i++) {
                            wr.write("0 ");
                        }
                    } else {
                        outArr(prev, wr);
                        wr.write("\n");
                        outArr(next, wr);
                    }
                } else {
                    wr.write("0\n0");
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

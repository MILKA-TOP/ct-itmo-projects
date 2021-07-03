package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


    public static void main(String[] args) {
        try (Scanner scan = new Scanner(new File("nextpartition.in"))) {
            try (FileWriter wr = new FileWriter("nextpartition.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                String line = scan.next();
                line = line.replaceAll("=", " ").replaceAll("\\+", " ");
                Scanner sc = new Scanner(line);
                int n = sc.nextInt();
                ArrayList<Integer> arr = new ArrayList<>();
                while (sc.hasNext()) {
                    arr.add(sc.nextInt());
                }
                if (arr.size() != 1) {
                    arr.set(arr.size() - 1, arr.get(arr.size() - 1) - 1);
                    arr.set(arr.size() - 2, arr.get(arr.size() - 2) + 1);
                    if (arr.get(arr.size() - 2) > arr.get(arr.size() - 1)) {
                        arr.set(arr.size() - 2, arr.get(arr.size() - 2) + arr.get(arr.size() - 1));
                        arr.remove(arr.size() - 1);
                    } else {
                        while (arr.get(arr.size() - 2) * 2 <= arr.get(arr.size() - 1)) {
                            arr.add(arr.get(arr.size() - 1) - arr.get(arr.size() - 2));
                            arr.set(arr.size() - 2, arr.get(arr.size() - 3));
                        }
                    }
                    wr.write(n + "=");
                    for (int i = 0; i < arr.size() - 1; i++) {
                        wr.write(arr.get(i) + "+");
                    }
                    wr.write(Integer.toString(arr.get(arr.size() - 1)));
                } else {
                    wr.write("No solution");
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

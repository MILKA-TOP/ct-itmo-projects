package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static int findLastIndex(int[] arr) {
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                k = i - 1;
                break;
            }
        }
        return k;
    }

    public static String arrToString(int[] arr, int lastIndex) {
        StringBuilder out = new StringBuilder();
        for (int i = lastIndex; i > -1; i--) {
            if (arr[i] >= 10) {
                out.append(":");
            }
            out.append(arr[i]);
            if (i != 0) {
                out.append("+");
            }
        }
        return out.toString();
    }

    public static int findIndexMin(int[] arr, int lastIndex) {
        int minIndex = arr[0], minValue = Integer.MAX_VALUE;
        for (int i = 0; i < lastIndex; i++) {
            if (arr[i] < minValue) {
                minValue = arr[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static int[] rev(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("partition.in"))) {
            try (FileWriter wr = new FileWriter("partition.out")) {





                int n = sc.nextInt(), lastIndex = n - 1, minIndex;
                int[] arr = new int[n];
                TreeMap<String, Integer> map = new TreeMap<>();

                arr = rev(arr);
                while (arr[0] != n) {
                    map.put(arrToString(arr, lastIndex), 1);
                    minIndex = findIndexMin(arr, lastIndex);
                    arr[minIndex]++;
                    arr[lastIndex]--;
                    if (arr[lastIndex] == 0) {
                        lastIndex--;
                    }
                    arr = clean(minIndex + 1, arr, lastIndex);
                    lastIndex = findLastIndex(arr);
                }
                map.put(arrToString(arr, lastIndex), 1);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    wr.write(entry.getKey().replaceAll(":", "") + "\n");
                }





            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static int[] clean(int index, int[] arr, int lastIndex) {
        int[] tempArr = new int[arr.length];
        for (int i = 0; i < index; i++) {
            tempArr[i] = arr[i];
        }
        int nowIndexTempArr = index;
        for (int i = index; i <= lastIndex; i++) {
            for (int j = 0; j < arr[i]; j++) {
                tempArr[nowIndexTempArr++] = 1;
            }
        }
        return tempArr;
    }

}

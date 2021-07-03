package dm;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static long[] sortingArr(long arr[]) {
        int maxRadix = 0;
        arr[1] = arr[arr.length - 1];
        String[] stringArr = new String[arr.length - 1];
        for (int i = 0; i < stringArr.length; i++) {
            stringArr[i] = Long.toString(arr[i]);
            if (stringArr[i].length() > maxRadix) {
                maxRadix = stringArr[i].length();
            }
        }
        for (int i = 0; i < stringArr.length; i++) {
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < maxRadix - stringArr[i].length(); j++) {
                temp.append("0");
            }
            temp.append(arr[i]);
            stringArr[i] = temp.toString();
        }
        stringArr = sortLSD(stringArr);
        long[] outArr = new long[stringArr.length];
        for (int i = 0; i < outArr.length; i++) {
            outArr[i] = Long.parseLong(stringArr[i]);
        }
        return outArr;
    }

    //if you're reading it, it's just an LSD sorting algorithm, don't ban it
    public static String[] sortLSD(String[] originalArray) {
        String[] sortedArray = new String[originalArray.length];
        for (int i = originalArray[0].length() - 1; i > -1; i--) {
            int[] countOfAlphs = new int[301];
            for (int j = 0; j < originalArray.length; j++) {
                countOfAlphs[originalArray[j].charAt(i) + 1]++;
            }
            for (int j = 0; j < 300; j++) {
                countOfAlphs[j + 1] += countOfAlphs[j];
            }
            for (int j = 0; j < originalArray.length; j++) {
                sortedArray[countOfAlphs[originalArray[j].charAt(i)]] = originalArray[j];
                countOfAlphs[originalArray[j].charAt(i)]++;
            }
            for (int j = 0; j < originalArray.length; j++) {
                originalArray[j] = sortedArray[j];
            }
        }
        return sortedArray;
    }

    public static List<String> addLZW(String line, List<String> alp) {
        StringBuilder partOfWord = new StringBuilder(Character.toString(line.charAt(0)));
        int i = 1;
        while (i <= line.length()) {
            partOfWord.append(Character.toString(line.charAt(i)));
            if (!isItInArray(alp, partOfWord.toString())) {
                alp.add(partOfWord.toString());
                partOfWord.setLength(partOfWord.length() - 1);

                partOfWord = new StringBuilder();
            } else {
                i++;
            }
            if (i == line.length()) {
                partOfWord.append(Character.toString(line.charAt(i - 1)));
                partOfWord.setLength(partOfWord.length() - 1);

                break;
            }
        }
        return alp;
    }

    public static boolean isItInArray(List<String> arr, String obj) {
        for (int i = 0; i < arr.size(); i++) {
            if (obj.equals(arr.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static int numOfObj(List<String> arr, String obj) {
        for (int i = 0; i < arr.size(); i++) {
            if (obj.equals(arr.get(i))) {
                return i;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), maxRadix = 0;
        long countsOut = 0;
        long[] tempArrOr = new long[n];
        for (int i = 0; i < n; i++) {
            tempArrOr[i] = sc.nextLong();
        }
        ////////////////////////////
        String[] stringArr = new String[n];
        for (int i = 0; i < n; i++) {
            stringArr[i] = Long.toString(tempArrOr[i]);
            if (stringArr[i].length() > maxRadix) {
                maxRadix = stringArr[i].length();
            }
        }
        for (int i = 0; i < n; i++) {
            StringBuilder temp = new StringBuilder();
            temp.append("0".repeat(Math.max(0, maxRadix - stringArr[i].length())));
            temp.append(tempArrOr[i]);
            stringArr[i] = temp.toString();
        }
        stringArr = sortLSD(stringArr);
        long[] arr = new long[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Long.parseLong(stringArr[i]);
        }
        ///////////////////////////////////
        while (arr.length != 1) {
            countsOut += arr[0] + arr[1];
            arr[0] += arr[1];
            arr = sortingArr(arr);
        }
        System.out.println(countsOut);
    }
}




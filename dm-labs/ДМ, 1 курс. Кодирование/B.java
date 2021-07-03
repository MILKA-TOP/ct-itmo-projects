package dm;

import java.util.Scanner;

public class Main {

    //if you're reading it, it's just an LSD sorting algorithm, don't ban it
    public static String[] sort(String[] originalArray) {
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.next();
        String[] allString = new String[line.length()];
        StringBuilder temp = new StringBuilder();
        String tempLine = line;
        for (int i = 0; i < line.length(); i++) {
            temp.append(tempLine.charAt(tempLine.length() - 1)).append(tempLine.substring(0, line.length() - 1));
            allString[i] = temp.toString();
            tempLine = temp.toString();
            temp = new StringBuilder();
        }
        allString = sort(allString);
/*       for (int i = 0; i < line.length(); i++) {
            System.out.println(allString[i]);
        }
        System.out.println("=======");
        allString = sort(allString);
        for (int i = 0; i < line.length(); i++) {
            System.out.println(allString[i]);
        }*/
        for (int i = 0; i < allString.length; i++) {
            System.out.print(allString[i].charAt(allString.length - 1));
        }
    }
}

package dm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


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
        String line = sc.nextLine();
        List<String> alp = new ArrayList<>();
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            alp.add(Character.toString((char) (i + 97)));
        }
/*        int i = 0;
        while (i < line.length()) {
            StringBuilder partOfWord = new StringBuilder();
            do {
                partOfWord.append(line.charAt(i++));
            } while (i != line.length() && isItInArray())

        }*/
        StringBuilder partOfWord = new StringBuilder(Character.toString(line.charAt(0)));
        int i = 1;
        if (line.length() > 1) {
            while (i <= line.length()) {
                partOfWord.append(Character.toString(line.charAt(i)));
                if (!isItInArray(alp, partOfWord.toString())) {
                    alp.add(partOfWord.toString());
                    partOfWord.setLength(partOfWord.length() - 1);
                    out.add(numOfObj(alp, partOfWord.toString()));
                    partOfWord = new StringBuilder();
                } else {
                    i++;
                }
                if (i == line.length()) {
                    partOfWord.append(Character.toString(line.charAt(i - 1)));
                    partOfWord.setLength(partOfWord.length() - 1);
                    out.add(numOfObj(alp, partOfWord.toString()));
                    break;
                }
            }
        } else {
            out.add(numOfObj(alp, Character.toString(line.charAt(0))));
        }
        for (Integer num :
                out) {
            System.out.print(num + " ");
        }
    }
}

package dm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Character> alp = new ArrayList<>();
        for (int i = 97; i < 97 + 26; i++) {
            alp.add((char) i);
        }
        List<Integer> outResult = new ArrayList<>();
        String line = sc.next();
        for (int i = 0; i < line.length(); i++) {
            int index = alp.indexOf(line.charAt(i));
            outResult.add(index);
            char tempChar = line.charAt(i);
            alp.remove(index);
            alp.add(0, tempChar);
        }
        for (Integer integer : outResult) {
            System.out.print((integer + 1) + " ");
        }
    }
}

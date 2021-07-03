package DISC;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class disc {

    static HashMap<String, HashMap<String, ArrayList<String>>> all = new HashMap<>();

    static String start;
    static String checkLine;
    static String[] smallArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static String[] bigArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    public static boolean check(String nowCheck, int positionOfElem) {
        if (positionOfElem == checkLine.length()) return false;
        String checkingValue = String.valueOf(checkLine.charAt(positionOfElem));
        if (nowCheck.equals("TERM") && positionOfElem == checkLine.length() - 1) {
            return true;
        };
        if (nowCheck.equals("TERM")) return false;
       // System.out.println(nowCheck);
        for (String check :
                all.get(nowCheck).get(checkingValue)) {
            if (check.equals("TERM") && positionOfElem == checkLine.length() - 1) {
                return true;
            };
            if (!check.equals("TERM") && check(check, positionOfElem + 1)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        String name = "automaton.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int n = sc.nextInt();
                start = sc.next();
                for (String value : bigArray) {
                    all.put(value, new HashMap<>());
                    for (String s : smallArray) {
                        all.get(value).put(s, new ArrayList<>());
                    }
                }
                for (int i = 0; i < n; i++) {
                    String from = sc.next();
                    sc.next();
                    String to = sc.next();
                    if (to.length() == 2) {
                        all.get(from).get(String.valueOf(to.charAt(0))).add(String.valueOf(to.charAt(1)));
                    } else {
                        all.get(from).get(String.valueOf(to.charAt(0))).add("TERM");
                    }

                }
                n = sc.nextInt();
                for (int i = 0; i < n; i++) {
                    checkLine = sc.next();
                    if (check(start, 0)) {
                        wr.write("yes\n");
                    } else {
                        wr.write("no\n");
                    }
                }


                //wr.write("");

                //wr.write(Long.toString(Math.floorMod(finalCounts, 1000000000 + 7)));

//////////////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
package DISC;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class disc {


    static String start;
    static String word;
    static TreeSet<String> finOut = new TreeSet<>();
    static String[] smallArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static String[] bigArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    static Map<String, ArrayList<String>> check = new HashMap<>();
    static TreeSet<String> summ = new TreeSet<>();
    static Map<String, long[][]> counting = new HashMap<>();

    public static void preCountingFunc() {
        for (int i = 0; i < word.length(); i++) {
            for (String alpElement :
                    bigArray) {
                for (String element :
                        check.get(alpElement)) {
                    if (Character.isLowerCase(element.charAt(0)) && element.charAt(0) == word.charAt(i)) {
                        counting.get(alpElement)[i][i]++;
                    }
                }
            }
        }
    }

    public static void countingFunc() {
        for (int i = 2; i <= word.length(); i++) {
            for (int j = 0; j < word.length() - i + 1; j++) {
                for (int k = j; k < j + i - 1; k++) {
                    for (String elementAlp :
                            bigArray) {
                        for (String element :
                                check.get(elementAlp)) {
                            if (Character.isUpperCase(element.charAt(0))) {
                                counting.get(elementAlp)[j][i + j - 1] = (counting.get(elementAlp)[j][i + j - 1]
                                        + ((counting.get(String.valueOf(element.charAt(0)))[j][k])
                                        * (counting.get(String.valueOf(element.charAt(1)))[k + 1][i + j - 1])) % (1000000007)
                                ) % (1000000007);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String name = "nfc.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                Scanner scanLine = new Scanner(sc.nextLine());
                int n = scanLine.nextInt();
                start = scanLine.next();

                summ.add(start);

                for (String s : bigArray) {
                    check.put(s, new ArrayList<>());
                }

                for (int i = 0; i < n; i++) {
                    scanLine = new Scanner(sc.nextLine());
                    String from = scanLine.next();
                    scanLine.next();
                    summ.add(from);
                    if (scanLine.hasNext()) {
                        String to = scanLine.next();
                        check.get(from).add(String.valueOf(to));
                        for (int j = 0; j < to.length(); j++) {
                            if (Character.isUpperCase(to.charAt(j))) summ.add(String.valueOf(to.charAt(j)));
                        }
                    } else {
                        check.get(from).add(" ");
                    }
                }

                word = sc.next();
                for (String s : bigArray) {
                    long[][] tempArr = new long[200][200];
                    counting.put(s, tempArr);
                }

                preCountingFunc();
                countingFunc();

                //          System.out.println(counting.get(start)[0][word.length() - 1]);
                wr.write(String.valueOf(counting.get(start)[0][word.length() - 1]));
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
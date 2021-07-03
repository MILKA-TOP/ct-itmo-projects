package DISC;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class disc {


    static String start;
    static String checkLine;
    static TreeSet<String> eps = new TreeSet<>();
    static String[] smallArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static String[] bigArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    static Map<String, ArrayList<String>> check = new HashMap<>();


    public static void main(String[] args) {
        String name = "epsilon.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                Scanner scanLine = new Scanner(sc.nextLine());
                int n = scanLine.nextInt();
                start = scanLine.next();
                for (String value : bigArray) {
                    check.put(value, new ArrayList<>());
                }
                for (int i = 0; i < n; i++) {
                    scanLine = new Scanner(sc.nextLine());
                    String from = scanLine.next();
                    scanLine.next();
                    if (scanLine.hasNext()) {
                        String to = scanLine.next();
                        check.get(from).add(String.valueOf(to));
                    } else {
                        eps.add(from);
                    }
                }

                for (int j = 0; j < 10; j++) {
                    for (String alpElem :
                            bigArray) {
                        if (eps.contains(alpElem)) continue;
                        for (String arrayElem :
                                check.get(alpElem)) {
                            boolean hasEps = true;
                            String bigString = arrayElem.toUpperCase();
                            if (!bigString.equals(arrayElem)) continue;
                            for (int i = 0; i < arrayElem.length(); i++) {
                                if (!eps.contains(String.valueOf(arrayElem.charAt(i)))) {
                                    hasEps = false;
                                    break;
                                }
                            }
                            if (hasEps) {
                                eps.add(alpElem);
                                break;
                            }
                        }
                    }
                }
                for (String el :
                        eps) {
                    wr.write(el + " ");
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
package DISC;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class disc {


    static String start;
    static String checkLine;
    static TreeSet<String> finOut = new TreeSet<>();
    static String[] smallArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static String[] bigArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    static Map<String, ArrayList<String>> check = new HashMap<>();
    static TreeSet<String> summ = new TreeSet<>();


    public static void main(String[] args) {
        String name = "useless.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                Scanner scanLine = new Scanner(sc.nextLine());
                int n = scanLine.nextInt();
                start = scanLine.next();

                Set<String> notTerm = new HashSet<>();
                Set<String> can = new HashSet<>();
                can.add(start);
                summ.add(start);

                for (int i = 0; i < n; i++) {
                    scanLine = new Scanner(sc.nextLine());
                    String from = scanLine.next();
                    scanLine.next();
                    if (!check.containsKey(from)) check.put(from, new ArrayList<>());
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

                for (int j = 0; j < 30; j++) {
                    for (String alpElem :
                            summ) {
                        if (notTerm.contains(alpElem) || !check.containsKey(alpElem)) continue;
                        for (String elemFromArr :
                                check.get(alpElem)) {
                            String downElem = elemFromArr.toLowerCase();
                            if (elemFromArr.equals(" ") || downElem.equals(elemFromArr)) {
                                notTerm.add(alpElem);
                                continue;
                            }
                            boolean norm = true;
                            for (int i = 0; i < elemFromArr.length(); i++) {
                                if (Character.isUpperCase(elemFromArr.charAt(i))) {
                                    if (!notTerm.contains(String.valueOf(elemFromArr.charAt(i)))) {
                                        norm = false;
                                        break;
                                    }
                                }
                            }
                            if (norm) {
                                notTerm.add(alpElem);
                            }
                        }
                    }
                }

                for (String check : summ) {
                    if (!notTerm.contains(check)) finOut.add(check);
                }

                //deleting

                for (String badVal :
                        finOut) {
                    summ.remove(badVal);
                    check.remove(badVal);
                    for (String elementFromSumm :
                            summ) {
                        if (!check.containsKey(elementFromSumm)) continue;
                        ArrayList<String> removeElements = new ArrayList<>();
                        for (String fromArr :
                                check.get(elementFromSumm)) {
                            if (fromArr.contains(badVal)) removeElements.add(fromArr);
                        }
                        for (String removingElement :
                                removeElements) {
                            check.get(elementFromSumm).remove(removingElement);
                        }
                        if (check.get(elementFromSumm).isEmpty()) check.remove(elementFromSumm);
                    }

                }

                /*for (String badVal :
                        finOut) {
                    for (String elem : summ) {

                        if (finOut.contains(elem)) {
                            check.remove(elem);
                            continue;
                        }
                        if (!check.containsKey(elem)) continue;

                        ArrayList<String> deleteElements = new ArrayList<>();

                        for (String arrElem : check.get(elem)) {
                            if (arrElem.contains(badVal)) {
                                //                             check.get(elem).remove(badVal);
                                deleteElements.add(badVal);
                            }
                        }
                        for (String deleteElement : deleteElements) {
                            check.get(elem).remove(deleteElement);
                        }

                        if (check.get(elem).isEmpty()) check.remove(elem);
                    }
                }*/

                for (int j = 0; j < 30; j++) {
                    for (String alpElem :
                            summ) {
                        if (!can.contains(alpElem) || !check.containsKey(alpElem)) continue;
                        for (String elemFromArr :
                                check.get(alpElem)) {
                            if (elemFromArr.equals(" ")) {
                                continue;
                            }
                            for (int i = 0; i < elemFromArr.length(); i++) {
                                if (Character.isUpperCase(elemFromArr.charAt(i))) {
                                    can.add(String.valueOf(elemFromArr.charAt(i)));
                                }
                            }
                        }
                    }
                }


                for (String check : summ) {
                    if (!can.contains(check)) finOut.add(check);
                }

                for (String a :
                        finOut) {
                    wr.write(a + " ");
                }

               /* for (int j = 0; j < 10; j++) {
                    for (String alpElem :
                            fromSumm) {
                        if (finOut.contains(alpElem)) continue;
                        for (String arrayElem :
                                check.get(alpElem)) {
                            boolean hasEps = true;
                            String bigString = arrayElem.toUpperCase();
                            if (!bigString.equals(arrayElem)) continue;
                            for (int i = 0; i < arrayElem.length(); i++) {
                                if (!finOut.contains(String.valueOf(arrayElem.charAt(i)))) {
                                    hasEps = false;
                                    break;
                                }
                            }
                            if (hasEps) {
                                finOut.add(alpElem);
                                break;
                            }
                        }
                    }
                }
                for (String el :
                        finOut) {
                    wr.write(el + " ");
                }*/


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
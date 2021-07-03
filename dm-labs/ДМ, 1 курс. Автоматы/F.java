package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class E {

    static int n1;
    static int n2;
    static int m;
    static int k;
    static int[] ass;
    static boolean[] checked;
    static boolean[][] Ter = new boolean[2][];


    static TreeSet<String> summ = new TreeSet<>();
    static List<HashMap<String, Integer>> beta1;
    static List<HashMap<String, Integer>> beta2;

    public static boolean dfs(int a, int b) {
        checked[a] = true;
        if (Ter[0][a] != Ter[1][b]) return false;
        ass[a] = b;
        boolean res = true;
        for (String s :
                summ) {
            if (beta1.get(a).containsKey(s)) {
                int first = beta1.get(a).get(s);
                if (!beta2.get(b).containsKey(s)) return false;
                int second = beta2.get(b).get(s);

                boolean s1 = false;
                boolean s2 = false;
                for (String line :
                        summ) {
                    if (beta1.get(a).containsKey(line) && beta1.get(a).get(line) == a) {
                        s1 = true;
                    }
                    if (beta2.get(b).containsKey(line) && beta2.get(b).get(line) == b) {
                        s2 = true;
                    }
                    if (s1 && s2) break;
                }

                if (s1 != s2) return false;

                if (checked[first]) res = res && (second == ass[first]);
                else res = res && dfs(first, second);
            }

        }

        return res;

    }

    public static void main(String[] args) {
        String name = "isomorphism.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int t1, t2;
                n1 = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();

                Ter[0] = new boolean[n1];
                beta1 = new ArrayList<>();
                for (int i = 0; i < n1; i++) {
                    beta1.add(new HashMap<>());
                }
                for (int i = 0; i < k; i++) {
                    Ter[0][sc.nextInt() - 1] = true;
                }
                t1 = k;
                for (int i = 0; i < m; i++) {
                    int a = sc.nextInt() - 1, b = sc.nextInt() - 1;
                    String c = sc.next();
                    beta1.get(a).put(c, b);
                    summ.add(c);
                }

                n2 = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();
                Ter[1] = new boolean[n2];
                t2 = k;
                beta2 = new Vector<>();
                for (int i = 0; i < n2; i++) {
                    beta2.add(new HashMap<>());
                }
                for (int i = 0; i < k; i++) {
                    Ter[1][sc.nextInt() - 1] = true;
                }
                boolean newElementInAlp = false;
                for (int i = 0; i < m; i++) {
                    int a = sc.nextInt() - 1, b = sc.nextInt() - 1;
                    String c = sc.next();
                    beta2.get(a).put(c, b);
                    if (!summ.contains(c)) {
                        newElementInAlp = true;
                        wr.write("NO");
                        break;
                    }
                    summ.add(c);
                }
                if (!newElementInAlp) {
                    checked = new boolean[n1];
                    ass = new int[n1];
                    if (n1 != n2 || t1 != t2 || !dfs(0, 0)) {
                        wr.write("NO");
                    } else {
                        wr.write("YES");
                    }
                }

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

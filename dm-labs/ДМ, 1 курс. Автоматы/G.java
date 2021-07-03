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
    static boolean[][] checked;
    static boolean[][] Ter = new boolean[2][];


    static String[] summ = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    static List<HashMap<String, Integer>> beta1;
    static List<HashMap<String, Integer>> beta2;

    public static boolean dfs() {
        int[] pair = {1, 1};
        Stack<int[]> stack = new Stack<>();
        stack.push(pair);
        while (!stack.empty()) {
            int[] nowPair = stack.pop();
            int first = nowPair[0], second = nowPair[1];
            if (Ter[0][first] != Ter[1][second]) return false;
            checked[first][second] = true;
            for (String s : summ) {
                if (!checked[beta1.get(first).get(s)][beta2.get(second).get(s)]) {
                    stack.push(new int[]{beta1.get(first).get(s), beta2.get(second).get(s)});
                }
            }
        }
        return true;

    }

    public static void main(String[] args) {
        String name = "equivalence.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                n1 = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();

                Ter[0] = new boolean[n1 + 1];
                beta1 = new ArrayList<>();
                for (int i = 0; i <= n1; i++) {
                    beta1.add(new HashMap<>());
                    for (String s :
                            summ) {
                        beta1.get(i).put(s, 0);
                    }
                }
                for (int i = 0; i < k; i++) {
                    Ter[0][sc.nextInt()] = true;
                }
                for (int i = 0; i < m; i++) {
                    int a = sc.nextInt(), b = sc.nextInt();
                    String c = sc.next();
                    beta1.get(a).put(c, b);
                }

                n2 = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();
                Ter[1] = new boolean[n2 + 1];
                beta2 = new Vector<>();
                for (int i = 0; i <= n2; i++) {
                    beta2.add(new HashMap<>());
                    for (String s :
                            summ) {
                        beta2.get(i).put(s, 0);
                    }
                }
                for (int i = 0; i < k; i++) {
                    Ter[1][sc.nextInt()] = true;
                }
                for (int i = 0; i < m; i++) {
                    int a = sc.nextInt(), b = sc.nextInt();
                    String c = sc.next();
                    beta2.get(a).put(c, b);
                }
                checked = new boolean[n1 + 1][n2 + 1];


                if (!dfs()) {
                    wr.write("NO");
                } else {
                    wr.write("YES");
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
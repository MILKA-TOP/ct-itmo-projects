package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class E {

    static int n;
    static int m;
    static int k;
    static boolean hasLoop = false;
    static boolean[] Ter;
    static boolean[] visited;
    static boolean[] realUsing;

    static Set<String> summ = new HashSet<>();
    static List<HashMap<String, Integer>> beta = new ArrayList<>();
    static List<HashMap<String, ArrayList<Integer>>> betaReverse = new ArrayList<>();

    public static void findRealUsing(int value) {
        if (visited[value]) return;
        visited[value] = true;
        realUsing[value] = true;
        for (String s : summ) {
            if (betaReverse.get(value).containsKey(s)) {
                for (Integer a :
                        betaReverse.get(value).get(s)) {
                    findRealUsing(a);
                }
            }
        }
    }

    public static void looping(int value) {
        if (!realUsing[value] || hasLoop) return;
        if (visited[value]) {
            hasLoop = true;
            return;
        }
        visited[value] = true;
        for (String s : summ) {
            if (beta.get(value).containsKey(s)) {
                looping(beta.get(value).get(s));
                if (hasLoop) return;
            }
        }
        visited[value] = false;
    }

    public static long counting(int value) {
        if (!realUsing[value]) return 0;
        long outCount = 0;
        if (Ter[value]) outCount++;
        for (String s : summ) {
            if (beta.get(value).containsKey(s)) {
                outCount = (outCount + counting(beta.get(value).get(s)) + 1000000007) % 1000000007;
            }
        }

        return outCount;
    }

    public static void main(String[] args) {
        String name = "problem3.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                n = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();

                for (int i = 0; i < n; i++) {
                    beta.add(new HashMap<>());
                    betaReverse.add(new HashMap<>());
                }

                Ter = new boolean[n];
                visited = new boolean[n];
                realUsing = new boolean[n];

                for (int i = 0; i < k; i++) {
                    Ter[sc.nextInt() - 1] = true;
                }

                for (int i = 0; i < m; i++) {
                    int a = sc.nextInt();
                    int b = sc.nextInt();
                    String c = sc.next();
                    beta.get(a - 1).put(c, b - 1);
                    if (!betaReverse.get(b-1).containsKey(c)) {
                        betaReverse.get(b-1).put(c, new ArrayList<>());
                    }
                    betaReverse.get(b-1).get(c).add(a-1);
                    summ.add(c);
                }

                for (int i = 0; i < n; i++) {
                    if (Ter[i]) {
                        findRealUsing(i);
                    }
                }
                visited = new boolean[n];
                looping(0);

                if (hasLoop) {
                    wr.write("-1");
                } else {
                    wr.write(Long.toString(Math.floorMod(counting(0), 1000000000 + 7)));
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
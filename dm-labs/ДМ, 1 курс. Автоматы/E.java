package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {


    static int n_nfa;
    static int m_nfa;
    static int k_nfa;
    static int l_nfa;
    static int Q;
    static boolean[] T;

    static Set<String> summ = new TreeSet<>();

    public static void main(String[] args) {
        String name = "problem5.";
        try (Scanner sc = new Scanner(new File(name + "in"))) {
            try (FileWriter wr = new FileWriter(name + "out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int a, b;
                String c;
                n_nfa = sc.nextInt();
                m_nfa = sc.nextInt();
                k_nfa = sc.nextInt();
                l_nfa = sc.nextInt();
                T = new boolean[n_nfa];
                Q = n_nfa;
                List<HashMap<String, ArrayList<Integer>>> beta = new ArrayList<>();
                Map<TreeSet<Integer>, HashMap<String, TreeSet<Integer>>> beta_new = new HashMap<>();
                Map<TreeSet<Integer>, Boolean> terminal_new = new HashMap<>();
                Set<TreeSet<Integer>> Qd = new HashSet<>();
                for (int i = 0; i < n_nfa; i++) {
                    beta.add(new HashMap<>());
                }

                for (int i = 0; i < k_nfa; i++) {
                    T[sc.nextInt() - 1] = true;
                }

                for (int i = 0; i < m_nfa; i++) {
                    a = sc.nextInt();
                    b = sc.nextInt();
                    c = sc.next();
                    summ.add(c);
                    if (!beta.get(a - 1).containsKey(c)) beta.get(a - 1).put(c, new ArrayList<>());
                    beta.get(a - 1).get(c).add(b - 1);
                }


                ////////
                TreeSet<Integer> ts = new TreeSet<>();
                ts.add(0);
                Stack<TreeSet<Integer>> P = new Stack<>();
                P.push(ts);
                Qd.add(ts);
                while (!P.empty()) {
                    TreeSet<Integer> pd = P.pop();
                    for (String cc :
                            summ) {
                        TreeSet<Integer> qd = new TreeSet<>();
                        for (int p :
                                pd) {
                            if (beta.get(p).containsKey(cc)) qd.addAll(beta.get(p).get(cc));
                        }
                        if (!beta_new.containsKey(pd)) beta_new.put(pd, new HashMap<>());
                        beta_new.get(pd).put(cc, qd);
                        if (!Qd.contains(qd)) {
                            Qd.add(qd);
                            P.push(qd);
                        }

                    }
                }
                HashMap<TreeSet<Integer>, long[]> array_count = new HashMap<>();
                for (TreeSet<Integer> tl :
                        Qd) {
                    array_count.put(tl, new long[l_nfa + 1]);
                    terminal_new.put(tl, false);
                }
                for (int i = 0; i < n_nfa; i++) {
                    if (T[i]) {
                        for (TreeSet<Integer> s : Qd) {
                            if (s.contains(i)) terminal_new.put(s, true);
                        }
                    }
                }
                array_count.get(ts)[0] = 1;
                for (int i = 0; i < l_nfa; i++) {
                    for (TreeSet<Integer> j : Qd) {
                        for (String s :
                                summ) {
                            if (beta_new.containsKey(j) && beta_new.get(j).get(s) != null) {
                                array_count.get(beta_new.get(j).get(s))[i + 1] = (array_count.get(beta_new.get(j).get(s))[i + 1] + array_count.get(j)[i] + 1000000000 + 7) % (1000000000 + 7);
                            }
                        }
                    }
                }


                long finalCounts = 0;
                for (TreeSet<Integer> i : Qd) {
                    if (terminal_new.get(i)) {
                        finalCounts += array_count.get(i)[l_nfa];
                    }
                }
                wr.write(Long.toString(Math.floorMod(finalCounts, 1000000000 + 7)));

//////////////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
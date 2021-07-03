package dm;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    static int n;
    static int m;
    static int k;
    static int l;
    static boolean[] t;
    static String[] alp = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("problem4.in"))) {
            try (FileWriter wr = new FileWriter("problem4.out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int a, b;
                String c;
                n = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();
                l = sc.nextInt();
                t = new boolean[n];
                List<HashMap<String, Integer>> beta = new ArrayList<>();
                long[][] array_count = new long[n][l + 1];
                array_count[0][0] = 1;
                for (int i = 0; i < n; i++) {
                    beta.add(new HashMap<>());
                    for (String s : alp) {
                        beta.get(i).put(s, -1);
                    }
                }

                for (int i = 0; i < k; i++) {
                    t[sc.nextInt() - 1] = true;
                }

                for (int i = 0; i < m; i++) {
                    a = sc.nextInt();
                    b = sc.nextInt();
                    c = sc.next();
                    beta.get(a - 1).put(c, b - 1);
                }
                for (int i = 0; i < l; i++) {
                    for (int j = 0; j < n; j++) {
                        for (String s :
                                alp) {
                            if (beta.get(j).get(s) != -1) {
                                array_count[beta.get(j).get(s)][i + 1] = (array_count[beta.get(j).get(s)][i + 1] + array_count[j][i] + 1000000000 + 7) % (1000000000 + 7);
                            }
                        }
                    }
                }


                long finalCounts = 0;
                for (int i = 0; i < n; i++) {
                    if (t[i]) {
                        finalCounts += array_count[i][l];
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
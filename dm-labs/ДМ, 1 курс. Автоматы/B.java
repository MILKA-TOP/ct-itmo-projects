package dm;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    static String line;
    static int n;
    static int m;
    static int k;
    static boolean[] t;
    static char[] alp = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    ;

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("problem2.in"))) {
            try (FileWriter wr = new FileWriter("problem2.out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int a, b;
                String c;
                line = sc.next();
                n = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();
                t = new boolean[n];
                List<HashMap<String, ArrayList<Integer>>> beta = new ArrayList<>();
                boolean[][] can = new boolean[line.length() + 1][n];
                for (int i = 0; i < n; i++) {
                    beta.add(new HashMap<>());
                    for (char value : alp) {
                        beta.get(i).put(String.valueOf(value), new ArrayList<>());
                    }
                }

                for (int i = 0; i < k; i++) {
                    t[sc.nextInt() - 1] = true;
                }
                for (int i = 0; i < m; i++) {
                    a = sc.nextInt();
                    b = sc.nextInt();
                    c = sc.next();
                    beta.get(a - 1).get(c).add(b - 1);

                }

                int cur = 0;
                can[0][0] = true;
                for (int i = 0; i < line.length(); i++) {
                    for (int q = 0; q < n; q++) {
                        if (can[i][q]) {
                            for (int r : beta.get(q).get(String.valueOf(line.charAt(i)))) {
                                can[i + 1][r] = true;
                            }
                        }

                    }
                }
                boolean completed = false;
                for (int q = 0; q < n; q++) {
                    if (can[line.length()][q] && t[q]) {
                        completed = true;
                        wr.write("Accepts");
                        break;
                    }
                }
                if (!completed) {
                    wr.write("Rejects");
                }

//////////////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
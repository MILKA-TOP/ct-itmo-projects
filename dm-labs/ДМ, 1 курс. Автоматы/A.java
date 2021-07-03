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

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("problem1.in"))) {
            try (FileWriter wr = new FileWriter("problem1.out")) {
//////////////////////////////////////////////////////////////////////////////////////////
                int a, b;
                String c;
                line = sc.next();
                n = sc.nextInt();
                m = sc.nextInt();
                k = sc.nextInt();
                t = new boolean[n];
                List<HashMap<String, Integer>> beta = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    beta.add(new HashMap<>());
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

                int cur = 0;
                OUT:
                while (true) {
                    for (int i = 0; i < line.length(); i++) {
                        if (beta.get(cur).containsKey(String.valueOf(line.charAt(i))))
                            cur = beta.get(cur).get(String.valueOf(line.charAt(i)));
                        else {
                            wr.write("Rejects");
                            break OUT;
                        }
                    }
                    if (t[cur]) {
                        wr.write("Accepts");
                    } else {
                        wr.write("Rejects");
                    }
                    break;
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
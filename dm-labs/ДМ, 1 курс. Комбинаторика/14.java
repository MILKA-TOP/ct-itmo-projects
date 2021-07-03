package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static long factorial(long n) {
        long a = 1;
        for (int i = 1; i <= n; i++) {
            a *= i;
        }
        return a;
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("perm2num.in"))) {
            try (FileWriter wr = new FileWriter("perm2num.out")) {
                int n = sc.nextInt();
//                String num = sc.next();
//                int n = 3;
                int[] counts = new int[n];
                long outNum = 0;
                for (int i = 0; i < n; i++) {
                    int nowNum = sc.nextInt();
                    int d = 0;
                    for (int j = 0; j < nowNum; j++) {
                        if (counts[j] == 0) {
                            d++;
                        }
                    }
                    counts[nowNum - 1]++;

                    outNum += (d - 1) * factorial(n - 1 - i);
                }

                wr.write(Long.toString(outNum));
//                System.out.println(outNum);


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
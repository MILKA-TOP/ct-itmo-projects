package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("gray.in"))) {
            try (FileWriter wr = new FileWriter("gray.out")) {

                int n = sc.nextInt(), counts = (int) Math.pow(2, n), countsNowCodes = 2, t;
                int[][] GrayArray = new int[counts + 1][n + 1];
                GrayArray[1][n] = 0;
                GrayArray[2][n] = 1;
                for (int i = 2; i <= n; i++) {
                    t = countsNowCodes;
                    countsNowCodes *= 2;
                    for (int k = (countsNowCodes / 2 + 1); k <= countsNowCodes; k++) {
                        GrayArray[k] = GrayArray[t].clone();
                        GrayArray[t][n - i + 1] = 0;
                        GrayArray[k][n - i + 1] = 1;
                        t--;
                    }
                }
                for (int i = 1; i <= counts; i++) {
                    for (int j = 1; j <= n; j++) {
                        wr.write(Integer.toString(GrayArray[i][j]));
                    }
                    wr.write("\n");
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

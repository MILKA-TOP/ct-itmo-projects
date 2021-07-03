package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("telemetry.in"))) {
            try (FileWriter wr = new FileWriter("telemetry.out")) {
                int n = sc.nextInt(), k = sc.nextInt(), counts = (int) Math.pow(k, n);
//                int[][] GrayCode = new int[counts][n];
                StringBuilder[] GrayCode = new StringBuilder[counts];
                for (int i = 0; i < k; i++) {
                    GrayCode[i] = new StringBuilder(Integer.toString(i));
                }
                int newNum = k, oldNum = k - 1;
                for (int i = 1; i < n; i++) {
                    int toNum = newNum;
                    for (int j = 1; j < k; j++) {
                        oldNum = newNum - 1;
                        for (int l = 0; l < toNum; l++) {
                            GrayCode[newNum] = new StringBuilder(GrayCode[oldNum]);
                            if (j == 1) {
                                GrayCode[newNum].insert(0, Integer.toString(j));
                                GrayCode[oldNum].insert(0, Integer.toString(0));
                            } else {
                                GrayCode[newNum].replace(0, 1, Integer.toString(j));
                            }
                            newNum++;
                            oldNum--;
                        }
                    }
                }


                for (int i = 0; i < counts; i++) {
                    wr.write(GrayCode[i].toString());
                    System.out.println(GrayCode[i]);

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

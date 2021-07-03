package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("brackets.in"))) {
            try (FileWriter wr = new FileWriter("brackets.out")) {

                int num = sc.nextInt() * 2, len = (int) Math.pow(2, num), nowLen, n = num / 2;
                for (int i = (int) Math.pow(2, n) - 1; i < len; i++) {
                    StringBuilder temp = new StringBuilder(Integer.toString(i, 2));
                    int countOne = 0;
                    for (int j = 0; j < temp.length(); j++) {
                        if (temp.charAt(j) == '1') {
                            countOne++;
                        }
                    }
                    if (countOne == n) {
                        while (temp.length() < num) {
                            temp.insert(0, "0");
                        }
                        int nowIndex = 0;
                        boolean doAppend = true;
                        for (int j = 0; j < temp.length(); j++) {
                            if (temp.charAt(j) == '0') {
                                nowIndex++;
                            } else {
                                nowIndex--;
                            }
                            if (nowIndex < 0) {
                                doAppend = false;
                                break;
                            }
                        }
                        if (doAppend && nowIndex == 0) {
                            for (int j = 0; j < num; j++) {
                                if (temp.charAt(j) == '0') {
                                    wr.write("(");
                                } else {
                                    wr.write(")");
                                }
                            }
                            wr.write("\n");
                        }
                    }
                }


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
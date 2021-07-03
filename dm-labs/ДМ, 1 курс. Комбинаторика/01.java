package markup;

import java.io.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("allvectors.in"))) {
            try (FileWriter wr = new FileWriter("allvectors.out")) {
                int num = sc.nextInt(), len = (int) Math.pow(2, num), nowLen;
                for (int i = 0; i < len; i++) {
                    StringBuilder temp = new StringBuilder(Integer.toString(i, 2));
                    nowLen = temp.length();
                    while (nowLen < num) {
                        wr.write('0');
                        nowLen++;
                    }
                    wr.write(temp.toString() + "\n");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

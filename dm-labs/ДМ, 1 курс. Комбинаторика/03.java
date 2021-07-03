package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static char oneToTwo(char a) {
        if (a == '0') {
            return '1';
        } else if (a == '1') {
            return '2';
        } else {
            return '0';
        }
    }

    public static StringBuilder change(StringBuilder num) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < num.length(); i++) {
            temp.append(oneToTwo(num.charAt(i)));
        }
        return temp;
    }

    public static StringBuilder[] compl(StringBuilder[] arr, int n, FileWriter wr) throws IOException {
        for (int i = 0; i < Math.pow(3, n - 1); i++) {
            StringBuilder temp = new StringBuilder();
            String myNum = new String(Integer.toString(i, 3));
            int nowLen = myNum.length();
            while (nowLen < n) {
                temp.append("0");
                nowLen++;
            }
            temp.append(myNum);
            wr.write(temp.toString() + "\n");
//            System.out.println(temp);
            StringBuilder fiirstChange = change(temp);
            wr.write(fiirstChange.toString() + "\n");
//            System.out.println(fiirstChange);
            wr.write(change(fiirstChange).toString() + "\n");
//            System.out.println(change(fiirstChange));
            arr[i] = temp;
        }

        return arr;
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("antigray.in"))) {
            try (FileWriter wr = new FileWriter("antigray.out")) {
                int n = sc.nextInt();
                StringBuilder[] vecFirst = new StringBuilder[(int) Math.pow(3, n - 1)];
                vecFirst = compl(vecFirst, n, wr);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

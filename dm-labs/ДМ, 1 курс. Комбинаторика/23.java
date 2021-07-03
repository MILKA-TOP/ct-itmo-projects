package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("nextvector.in"))) {
            try (FileWriter wr = new FileWriter("nextvector.out")) {

                StringBuilder line = new StringBuilder(sc.next());
                int lastIndex = line.length() - 1;
                if (line.toString().equals("0")) {
                    wr.write("-\n1");
                } else {
                    if (line.charAt(lastIndex) == '1') {
                        StringBuilder first = new StringBuilder(line);
                        first.setCharAt(lastIndex, '0');
                        wr.write(first.toString() + "\n");
                        line.setCharAt(lastIndex, '0');
                        int nowIndex = lastIndex - 1;
                        while (nowIndex >= 0) {
                            if (line.charAt(nowIndex) == '1') {
                                line.setCharAt(nowIndex, '0');
                            } else {
                                break;
                            }
                            nowIndex--;
                        }
                        StringBuilder second = new StringBuilder(line);
                        if (nowIndex == -1) {
                            wr.write("-");
                        } else {
                            second.setCharAt(nowIndex, '1');
                            wr.write(second.toString());
                        }

                    } else {
                        StringBuilder second = new StringBuilder(line);
                        second.setCharAt(lastIndex, '1');
                        line.setCharAt(lastIndex, '1');
                        int nowIndex = lastIndex - 1;
                        while (nowIndex >= 0) {
                            if (line.charAt(nowIndex) == '0') {
                                line.setCharAt(nowIndex, '1');
                            } else {
                                break;
                            }
                            nowIndex--;
                        }
                        if (nowIndex == -1) {
                            wr.write("-\n" + second.toString());
                        } else {
                            line.setCharAt(nowIndex, '0');
                            StringBuilder first = new StringBuilder(line);

                            wr.write(first.toString() + "\n" + second.toString());
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
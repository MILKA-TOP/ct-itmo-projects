package md2html;

import java.io.*;
import java.util.Scanner;

public class Md2Html {


    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File(args[0]))) {
            try (Writer fw = new FileWriter(args[1])) {
                StringBuilder text = new StringBuilder();
                while (sc.hasNextLine()) {
                    text.append(sc.nextLine() + "\n");
                }
                String s = new Md2HtmlConvert ().convert(text.deleteCharAt(text.length() - 1).toString());
                fw.write(s);
                System.out.println(s);
//                    fw.write(text.toString());
            }

        } catch (IOException ex) {
            System.err.print(ex);
        }
    }

}

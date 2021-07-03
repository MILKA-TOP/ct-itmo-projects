package com.company;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("nextbrackets.in"))) {
            try (FileWriter wr = new FileWriter("nextbrackets.out")) {
                /////////////////////////////////////////////////////////////////////////////////
                StringBuilder line = new StringBuilder(sc.next());
                int countOfOpenBrack = 0, countOfCloseBrack = 0;
                for (int i = line.length() - 1; i > -1; i--) {
                    if (line.charAt(i) == '(') {
                        countOfOpenBrack++;
                        if (countOfCloseBrack > countOfOpenBrack) {
                            break;
                        }
                    } else {
                        countOfCloseBrack++;
                    }
                }
                line.delete(line.length() - countOfCloseBrack - countOfOpenBrack, line.length() + 1);
                if (line.equals("")) {
                    wr.write("-");
                } else {
                    line.append(")");
                    for (int i = 1; i < countOfOpenBrack + 1; i++) {
                        line.append("(");
                    }
                    for (int i = 1; i < countOfCloseBrack; i++) {
                        line.append(")");
                    }
                    if (line.charAt(0) == ')') {
                        wr.write("-");
                    } else {
                        wr.write(line.toString());
                    }
                }
                /////////////////////////////////////////////////////////////////////////////////
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}

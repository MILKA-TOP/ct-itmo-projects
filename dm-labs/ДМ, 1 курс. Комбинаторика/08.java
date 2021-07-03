package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(Integer.toString(arr[i]) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("choose.in"))) {
            try (FileWriter wr = new FileWriter("choose.out")) {
                int n = sc.nextInt(), k = sc.nextInt();
//                int n = 4, k = 4;
                int[] arr = new int[k];
                for (int i = 0; i < k - 1; i++) {
                    arr[i] = i + 1;
                }
                if (k != 1) {
                    arr[k - 1] = arr[k - 2] + 1;
                } else {
                    arr[0] = 1;
                }
                int nowIndexChange = k - 1, coint = 0;
                while (arr[0] <= n) {
                    while (arr[nowIndexChange] <= n) {
                        if (coint != 0) {
                            wr.write("\n");
                        }
                        for (int i = 0; i < arr.length - 1; i++) {
                            wr.write(Integer.toString(arr[i]) + " ");
                        }
                        wr.write(Integer.toString(arr[arr.length- 1]));
//                        print(arr);
                        arr[nowIndexChange]++;
                        coint++;
                    }
                    while (nowIndexChange >= 0) {
                        if (arr[nowIndexChange] >= n + nowIndexChange - k + 1) {


                            nowIndexChange--;
                        } else {
                            break;
                        }
                    }
                    if (nowIndexChange == -1) {
                        break;
                    }
                    arr[nowIndexChange]++;
                    for (int i = nowIndexChange + 1; i < k; i++) {
                        arr[i] = arr[i - 1] + 1;
                    }
                    nowIndexChange = k - 1;
                }


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
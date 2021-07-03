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
        try (Scanner sc = new Scanner(new File("permutations.in"))) {
            try (FileWriter wr = new FileWriter("permutations.out")) {
                int n = sc.nextInt(), k = n;
//                int n = 1, k = n;
                int[] check = new int[n + 1];
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
                        check = new int[n + 1];
                        boolean out = true;
                        for (int i = 0; i < k; i++) {
                            check[arr[i]]++;
                            if (check[arr[i]] == 2) {
                                out = false;
                                break;
                            }
                        }
                        if (out) {
                            for (int i = 0; i < arr.length; i++) {
                                wr.write(Integer.toString(arr[i]) + " ");
                            }
                            wr.write("\n");
                        }
                        arr[nowIndexChange]++;
                    }
                    while (nowIndexChange >= 0) {
                        if (arr[nowIndexChange] >= n) {
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
                        arr[i] = 1;
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
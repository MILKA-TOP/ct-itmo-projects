package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void print(long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(Long.toString(arr[i]) + " ");
        }
        System.out.println();
    }

    public static long factorial(long n) {
        long a = 1;
        for (int i = 1; i <= n; i++) {
            a *= i;
        }
        return a;
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("num2perm.in"))) {
            try (FileWriter wr = new FileWriter("num2perm.out")) {
                long n = sc.nextLong(), k = n, kOut = sc.nextLong(), factor = factorial(n - 1);
//                long n = 18, k = n, kOut = 355687428096000, factor = factorial(n - 1);
                int[] check = new int[(int) (n + 1)];
                for (long i = 1; i <= n; i++) {
                    factor = factorial(n - i);
                    long num = 1;
                    long counts = 0;
                    while ((num - 1) * factor <= kOut) {
                        num++;
                        counts += factor;
                    }
                    num--;
                    counts -= factor;
                    long f = 0, s = 1;
                    while (true) {
                        if (check[(int) s] == 0) {
                            f++;
                        }
                        if (f == num) {
                            check[(int) s] = 1;
//                            System.out.print(Long.toString(s) + " ");
                            wr.write(s + " ");
                            break;
                        }
                        s++;
                    }

                    kOut -= counts;
                }
/*
                int count = factor - plus;
                arr[0] = firstNum;
                for (int i = 1; i < k - 1; i++) {
                    arr[i] = 1;
                }
                if (k != 1) {
                    arr[k - 1] = 1;
                } else {
                    arr[0] = 1;
                }
                int nowIndexChange = k - 1;
                OUT:
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
                            if (count == kOut) {
                                for (int i = 0; i < arr.length; i++) {
                                    wr.write(Integer.toString(arr[i]) + " ");
//                                    System.out.print(Integer.toString(arr[i]) + " ");
                                }
                                break OUT;
                            }
                            count++;
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
                }*/


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
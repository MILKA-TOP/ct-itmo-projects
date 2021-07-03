package markup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {


    public static String add(int[] arr) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 10) {
                out.append(":");
            }
            out.append(arr[i]).append(" ");
        }
        return out.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("subsets.in"))) {
            try (FileWriter wr = new FileWriter("subsets.out")) {
                int n = sc.nextInt();
//                int n = 10;
                Map<String,Integer> outMap = new TreeMap<>();
                for (int k = 1; k <= n; k++) {
                    int[] arr = new int[k];
                    for (int i = 0; i < k - 1; i++) {
                        arr[i] = i + 1;
                    }
                    if (k != 1) {
                        arr[k - 1] = arr[k - 2] + 1;
                    } else {
                        arr[0] = 1;
                    }
                    int nowIndexChange = k - 1;
                    while (arr[0] <= n) {
                        while (arr[nowIndexChange] <= n) {

                            outMap.put(add(arr), 1);
                            arr[nowIndexChange]++;
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
                }
                wr.write("\n");
  //              System.out.println();
                for (Map.Entry<String, Integer> entry : outMap.entrySet()) {
                    wr.write(entry.getKey().replaceAll(":","") + "\n");
//                    System.out.println(entry.getKey().replaceAll(":",""));
                }



            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
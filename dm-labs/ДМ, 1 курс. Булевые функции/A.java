package markup;

import org.w3c.dom.ls.LSOutput;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] arr1 = new int[n][n];
        int[][] arr2 = new int[n][n];
        int[][] multArr = new int[n][n];
        int[] inf1 = new int[5];    //Реф, антиРеф, Симм, АнтиСим, Транз
        int[] inf2 = new int[5];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr1[i][j] = sc.nextInt();
            }
        }
        inf1 = anal(arr1, n);
        for (int i = 0; i < 5; i++) {
            System.out.print(Integer.toString(inf1[i]) + " ");
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr2[i][j] = sc.nextInt();
            }
        }
        inf2 = anal(arr2, n);
        for (int i = 0; i < 5; i++) {
            System.out.print(Integer.toString(inf2[i]) + " ");
        }
        System.out.println();
        multArr = multip(arr1, arr2, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(Integer.toString(multArr[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private static int[] anal(int[][] arr, int n) {
        int[] coutArr = new int[5];
        coutArr[0] = refFind(arr, n);
        coutArr[1] = antiRefFind(arr, n);
        int[] tempArr = new int[2];
        tempArr = symAnalFind(arr, n);

        coutArr[2] = tempArr[0];
        coutArr[3] = tempArr[1];
        coutArr[4] = tranFind(arr, n);

        return coutArr;
    }

    private static int tranFind(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (arr[i][j] == 1 && i != j) {
                    for (int k = 0; k < n; k++) {
                        if (arr[j][k] == 1 && arr[i][k] == 0 && k != j) {
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    private static int[] symAnalFind(int[][] arr, int n) {
        int sym = 1, anti = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i][j] == arr[j][i] && arr[i][j] == 1) {
                    anti = 0;
                }
                if ((arr[i][j] != arr[j][i])) {
                    sym = 0;
                }
            }
        }
        int[] cout = new int[2];
        cout[0] = sym;
        cout[1] = anti;
        return cout;
    }


    private static int antiSymFind(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && arr[i][j] == arr[j][i] && arr[i][j] == 1) {
                    return 0;
                }
            }
        }
        return 1;
    }

    private static int symFind(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (arr[i][j] != arr[j][i]) {
                    return 0;
                }
            }
        }
        return 1;
    }

    private static int antiRefFind(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            if (arr[i][i] != 0) {
                return 0;
            }
        }
        return 1;
    }

    private static int refFind(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            if (arr[i][i] != 1) {
                return 0;
            }
        }
        return 1;
    }

    private static int[][] multip(int[][] arr1, int[][] arr2, int n) {
        int[][] coutArr = new int[n][n];
        int elem;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                elem = 0;
                for (int k = 0; k < n; k++) {
                    elem += arr1[i][k] * arr2[k][j];
                }
                if (elem > 0) {
                    elem = 1;
                }
                coutArr[i][j] = elem;
            }
        }
        return coutArr;
    }
}

package com.company;

import java.util.Scanner;

public class Sum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), k;
        String s1;
        int[] outArr = new int[5];
        char[] s;
        for (int i = 0; i < n; i++) {
            k = sc.nextInt();
            s1 = sc.next();
            s = s1.toCharArray();
            if (k == 0 && Integer.parseInt(s1) == 0) {
                outArr[1] = 1;
                outArr[2] = 1;
            } else if (k == 0 && Integer.parseInt(s1) == 1) {
                outArr[0] = 1;
                outArr[2] = 1;
            } else {
                if (outArr[0] != 1) {
                    outArr[0] = save0(s);
                }
                if (outArr[1] != 1) {
                    outArr[1] = save1(s);
                }
                if (outArr[2] != 1) {
                    outArr[2] = neSam(k, s);
                }
                if (outArr[3] != 1) {
                    outArr[3] = mon(k, s);
                }
                if (outArr[4] != 1) {
                    outArr[4] = linPolinom(k, s);
                }
            }
        }
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += outArr[i];
//            System.out.print(Integer.toString(outArr[i]) + " ");
        }
//        System.out.println();
        if (sum == 5) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }

    }

    private static int linPolinom(int k, char[] s) {
        int[] polinom = new int[(int) Math.pow(2, k)];
        int[] tempPolinom = new int[(int) Math.pow(2, k)];
        int[] secondTempPolinom = new int[(int) Math.pow(2, k)];
        int n = (int) Math.pow(2, k);
        for (int i = 0; i < n; i++) {
            tempPolinom[i] = Integer.parseInt(String.valueOf(s[i]));
        }
        polinom[0] = tempPolinom[0];
        int cou = n - 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < cou; j++) {
                secondTempPolinom[j] = tempPolinom[j] ^ tempPolinom[j + 1];
            }
            tempPolinom = secondTempPolinom;
            secondTempPolinom = new int[10000];
            polinom[i] = tempPolinom[0];
            cou--;
        }
        int i = 0;
        int temp = 0;
        while (i < n) {
            if (i == Math.pow(2, temp)) {
                temp += 1;
            } else if (i != 0 && polinom[i] == 1) {
                if (polinom[i] == 1) {
                    return 1;
                }
            }
            i += 1;
        }
        return 0;
    }

    private static int neSam(int k, char[] s) {
        int j = 0;
        int cout = 0;
        while (true) {
            if (j == Math.pow(2, k - 1)) {
                break;
            }
            if (Integer.parseInt(String.valueOf(s[j])) == Integer.parseInt(String.valueOf(s[s.length - 1 - j]))) {
                cout = 1;
                break;
            }
            j += 1;
        }
        return cout;
    }

    private static int save0(char[] s) {
        if (Integer.parseInt(String.valueOf(s[0])) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int save1(char[] s) {
        if (Integer.parseInt(String.valueOf(s[s.length - 1])) == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int mon(int k, char[] s) {
        int j = 0, plus, index, firstEnd;
        int cout = 0, tempIndex;
        while (j < k) {
            plus = (int) Math.pow(2, j);
            index = 0;
            firstEnd = index + plus;
            while ((index + plus) < Math.pow(2, k)) {
                tempIndex = index;
                while (index < firstEnd && index + plus < Math.pow(2, k)) {
                    if (Integer.parseInt(String.valueOf(s[index])) > Integer.parseInt(String.valueOf(s[index + plus]))) {
                        cout = 1;
                        break;
                    }
                    index += 1;
                }
                index += plus;
                firstEnd = index + plus;
            }
            if (cout == 1) {
                break;
            }
            j++;
        }
        return cout;
    }
}
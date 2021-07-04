package com.company;

import java.util.Scanner;

public class Main {

    public static double f(double x) {
        return Math.pow(x, 3);
    }

    public static void printing() {
        System.out.println("Введите способ выбора оснащений (Ввод без скобок):");
        System.out.println("[0] - Левые оснащения");
        System.out.println("[1] - Правые оснащения");
        System.out.println("[2] - Средние оснащения");
        System.out.println("[3] - Случайные оснащения");
    }

    public static double getEps(int type, double a, double b, double nowI, double n, int counter) {
        switch (type) {
            case 0:
                return nowI;
            case 1:
                return (nowI + (b - a) / n);
            case 2:
                return (2 * nowI + (b - a) / n) / 2;
            case 3:
                return counter * ((b - a) / n) + Math.random() * ((b - a) / n);
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите число разбиений");
        double n = sc.nextDouble(), a = 0, b = 2;

        int type, counter = 0;
        double endSum = 0;

        OUT:
        while (true) {
            printing();
            type = sc.nextInt();
            switch (type) {
                case 0:
                case 1:
                case 2:
                case 3:
                    break OUT;
                default:
                    System.out.println("Введено неверное число");
            }
        }

        for (double i = a; i < b; i += (b - a) / n) {

            double eps = getEps(type, a, b, i, n, counter++);

            endSum += f(eps) * ((b - a) / n);
        }

        System.out.println(endSum);
    }
}


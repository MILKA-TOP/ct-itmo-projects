package markup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static boolean isInList(List<StringBuilder> list, StringBuilder object, int n) {
        for (int i = 2 * n; i < list.size(); i++) {
            if (object.toString().equals(list.get(i).toString())) {
                return true;
            }
        }
        return false;
    }

    public static int indexOfList(List<StringBuilder> list, StringBuilder object, int n) {
        for (int i = 2 * n; i < list.size(); i++) {
            if (object.toString().equals(list.get(i).toString())) {
                return i;
            }
        }
        return -1;
    }

    public static List<StringBuilder> addSdnf(StringBuilder funcxi, int n, List<StringBuilder> sdnf) {
        StringBuilder temp = new StringBuilder();
        int lastAppend = -1;
        temp.append(2).append(" ");
        if (funcxi.charAt(0) == '0') {
            temp.append(1 + n).append(" ");
        } else {
            temp.append(1).append(" ");
        }
        for (int i = 1; i < n; i++) {
            if (funcxi.charAt(i) == '0') {
                temp.append(n + i + 1);
            } else {
                temp.append(i + 1);
            }
            if (!isInList(sdnf, temp, n)) {
                sdnf.add(temp);
                lastAppend = sdnf.size();
            } else {
                lastAppend = indexOfList(sdnf, temp, n) + 1;
            }
            temp = new StringBuilder();
            temp.append(2).append(" ").append(lastAppend).append(" ");
        }
        return sdnf;
    }

    public static List<StringBuilder> lastSknf(List<StringBuilder> sdnf, List<Integer> lastIndex) {
        StringBuilder temp = new StringBuilder();
        temp.append(3).append(" ").append(lastIndex.get(0));
        for (int i = 1; i < lastIndex.size(); i++) {
            temp.append(" ").append(lastIndex.get(i));
            sdnf.add(temp);
            temp = new StringBuilder();
            temp.append(3).append(" ").append(sdnf.size());
        }
        return sdnf;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder temp;
        StringBuilder[] funcxi = new StringBuilder[(int) Math.pow(2, n)];
        List<StringBuilder> sdnf = new ArrayList<>();
        List<Integer> lastIndex = new ArrayList<>();
        int[] func = new int[(int) Math.pow(2, n)];
        for (int i = 0; i < (Math.pow(2, n)); i++) {
            funcxi[i] = new StringBuilder(sc.next());
            func[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            sdnf.add(new StringBuilder(i));
        }
        for (int i = 0; i < n; i++) {
            temp = new StringBuilder();
            temp.append(1).append(" ").append(i + 1);
            sdnf.add(temp);
        }
        for (int i = 0; i < Math.pow(2, n); i++) {
            if (func[i] == 1) {
                sdnf = addSdnf(funcxi[i], n, sdnf);
                lastIndex.add(sdnf.size());
            }
        }


        if (sdnf.size() == 2 * n) {
            System.out.println(n + 2);
            System.out.println("1 1");
            System.out.println(Integer.toString(2) + " 1 " + Integer.toString(n + 1) );
        } else {
            sdnf = lastSknf(sdnf, lastIndex);
            System.out.println(sdnf.size());
            for (int i = n; i < sdnf.size(); i++) {
                System.out.println(sdnf.get(i));
            }
        }
    }
}

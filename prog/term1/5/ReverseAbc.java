import java.util.*;

public class ReverseAbc {
	
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        ArrayList<ArrayList<String>> nums = new ArrayList<>();
        int i = 0;
        int max = 0;
		int countLines = 0;
		int j = 0;
        while (sc.hasNext()) {
            String l = sc.nextLine();
            Scanner line = new Scanner(l);
            nums.add(new ArrayList<>());
            while (line.hasNext()) {
                nums.get(countLines).add(line.next());
            }
			countLines++;
        }
        for (j = nums.size() - 1; j >= 0; j--) {
            for (int k = nums.get(j).size() - 1; k >= 0; k--) {
                System.out.print(nums.get(j).get(k) + " ");
            }
            System.out.println();
        }
    }
}
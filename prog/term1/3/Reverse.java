import java.util.*;

public class Reverse{
  
    public static void main(String[] args){
		StringBuilder str = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		System.out.println(sc.nextLine());
		System.out.println(sc.nextLine());
		StringBuilder line = new StringBuilder();
		Scanner numb = new Scanner(line.toString());
		int[] minimumLine = new int[10000];
		int[] minimumPost = new int[10000];
		int[] countElements = new int[10000];
		int minLine = Integer.MAX_VALUE;
		int cinNumber;
		int numPost;
		int coutMin;
		int countLines = 0;
		int maxCountPost = 0;
		    while (sc.hasNextLine()){
				numPost = 0;
				minLine = Integer.MAX_VALUE;
				countLines++;
				line.setLength(0);
				line.append(sc.nextLine());
				numb = new Scanner(line.toString());
				while (numb.hasNextInt()){
					cinNumber = numb.nextInt();
					if (cinNumber < minLine){
					    minLine = cinNumber;
					}
					if (maxCountPost >= (numPost + 1)){
					    if (cinNumber < minimumPost[numPost]){
							minimumPost[numPost] = cinNumber;
					    }
					} else{
					    minimumPost[maxCountPost] = cinNumber;
					    maxCountPost++;
					}  
					str.insert(0, Integer.toString(cinNumber) + " "); 
					numPost++;
				}
				countElements[countLines - 1] = numPost;
				minimumLine[countLines - 1] = minLine;
				if (sc.hasNextLine()){
					str.insert(0, "\n");
				}
		    }
		for (int i = 0; i < countLines; i++){
		    for (int j = 0; j < countElements[i]; j++){
				coutMin = Math.min(minimumLine[i], minimumPost[j]);
				System.out.print(Integer.toString(coutMin) + " ");
		    }
		    System.out.print("\n");
		}
    }
}
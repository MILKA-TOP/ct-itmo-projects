import java.util.*;

public class ReverseMin{
	
  public static int returnMin(int a, int b){
	if (a >= b){
	  return b;
	} else{
	  return a;
	}
  }
	
  public static void main(String[] args){
	StringBuilder str = new StringBuilder();
    Scanner sc = new Scanner(System.in);
	StringBuilder line = new StringBuilder();
	Scanner numb = new Scanner(line.toString());
//	ArrayList<ArrayList<Integer>> fullMat = new ArrayList<ArrayList<Integer>>();
//	ArrayList<Integer> oneLine = new ArrayList<Integer>();
	ArrayList<Integer> minimumLine = new ArrayList<Integer>();
	ArrayList<Integer> minimumPost = new ArrayList<Integer>();
	ArrayList<Integer> countElements = new ArrayList<Integer>();
	int minLine = Integer.MAX_VALUE;
	int cinNumber;
	int numPost;
	int coutMin;
	int countLines = 0;
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
			if (minimumPost.size() >= (numPost + 1)){
				if (cinNumber < minimumPost.get(numPost)){
					minimumPost.set(numPost, cinNumber);
				}
			} else{
				minimumPost.add(cinNumber);
			}	
//			oneLine.add(cinNumber);
			str.insert(0, Integer.toString(cinNumber) + " "); 
			numPost++;
		}
		countElements.add(numPost);
		minimumLine.add(minLine);
//		fullMat.add(oneLine);
		if (sc.hasNextLine()){
			str.insert(0, "\n");
		}
//		oneLine = new ArrayList<Integer>();
    }
	for (int i = 0; i < countLines; i++){
		for (int j = 0; j < countElements.get(i); j++){
			coutMin = returnMin(minimumLine.get(i), minimumPost.get(j));
			System.out.print(Integer.toString(coutMin) + " ");
		}
		System.out.print("\n");
	}
  }
}
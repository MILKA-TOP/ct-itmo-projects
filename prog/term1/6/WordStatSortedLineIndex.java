import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordStatSortedLineIndex {

    public static void main(String[] args) {

        try (FileReader reader = new FileReader(args[0], StandardCharsets.UTF_8)) {
            int newCountPartOfWord = 0;
            Scanner sc = new Scanner(reader);
			Scanner liner;
			StringBuilder temp;
            StringBuilder tempWord = new StringBuilder();
			int numLine = 0;
			int numNumber;
			HashMap<String, StringBuilder> wordStat = new HashMap<>();
			HashMap<String, Integer> counter = new HashMap<>();
            while (sc.hasNextLine()) {
				liner = new Scanner(sc.nextLine());
				numLine++;
				numNumber = 1;
				while (liner.hasNext()) {
					tempWord = new StringBuilder(liner.nextWord().toLowerCase());
					if (tempWord.length() != 0) {
						if (!wordStat.containsKey(tempWord.toString())) {
							temp = new StringBuilder();
							temp.append(" ").append(numLine).append(":").append(numNumber);
							wordStat.put(tempWord.toString(), temp);
							counter.put(tempWord.toString(), 1);
						} else {
							temp = new StringBuilder(wordStat.get(tempWord.toString()));
							temp.append(" ").append(numLine).append(":").append(numNumber);
							wordStat.replace(tempWord.toString(), temp);
							counter.replace(tempWord.toString(), counter.get(tempWord.toString()) + 1);
						}
						numNumber++;
					}
				}
				
            }
			TreeMap<String, StringBuilder> sortMap = new TreeMap<>();
			sortMap.putAll(wordStat);
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"))) {
                Set<Map.Entry<String, StringBuilder>> mapping = sortMap.entrySet();
				for (Map.Entry<String, StringBuilder> cout: mapping) {
					if (cout.getKey() != " ") {
						writer.write(cout.getKey() + " " + counter.get(cout.getKey()) + cout.getValue() + "\n");
						System.out.print(cout.getKey() + " " + counter.get(cout.getKey()) + cout.getValue() + "\n");
					}
				}
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
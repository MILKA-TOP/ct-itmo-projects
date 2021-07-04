import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordStatInputShingles{
  
  public static void main(String[] args){
    
    try(FileReader reader = new FileReader(args[0], StandardCharsets.UTF_8); BufferedReader bufferreader = new BufferedReader(reader)) {   
      char tempChar;
      int line;
      int newCountPartOfWord = 0;
      StringBuilder tempWord = new StringBuilder();
      StringBuilder tempPartOfWord;
      String[] newPartOfWordModification = new String[10000];
      int[] countNewPartOfWordModification = new int[10000];
      int i;
      line = bufferreader.read();
      while (line != -1) {
        tempChar = Character.toLowerCase((char)line);
        while ((tempChar == '\'' || Character.getType(tempChar) == Character.DASH_PUNCTUATION || Character.isAlphabetic(tempChar))) {
          tempWord.append(tempChar);
          line = bufferreader.read();
          if (line != -1) {
            tempChar = Character.toLowerCase((char)line);  
          } else {
            break;
          }
          
        }
        if ((!tempWord.toString().isEmpty()) && (tempWord.length() >= 3)) {
          for (int j = 0; j < tempWord.length() - 2; j++) {
            tempPartOfWord = new StringBuilder(tempWord.toString().substring(j,j+3));
            if (!Arrays.asList(newPartOfWordModification).contains(tempPartOfWord.toString())) {
              newPartOfWordModification[newCountPartOfWord] = tempPartOfWord.toString();
              countNewPartOfWordModification[newCountPartOfWord] = 1;
              newCountPartOfWord++;
            } else {
              countNewPartOfWordModification[Arrays.asList(newPartOfWordModification).indexOf(tempPartOfWord.toString())] += 1;
            }
          }
        }
        tempWord = new StringBuilder();
        line = bufferreader.read();
      }
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"))) {
        for (i = 0; i < newCountPartOfWord; i++) {
          writer.write(newPartOfWordModification[i] + " " + countNewPartOfWordModification[i] + '\n');
        }
      } catch(IOException ex) {
        System.out.println(ex.getMessage());
      }
    } catch(IOException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
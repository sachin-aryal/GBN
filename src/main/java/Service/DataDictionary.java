package Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: SACHIN
 * Date: 7/6/2016.
 */
public class DataDictionary {


    public static Hashtable<String,Double> wordDictionary = new Hashtable<>();
    public static List<String> wordsTobeIgnored = new ArrayList<>(Arrays.asList("the","is","a","an"));
    public static List<String> negationWord = new ArrayList<>();


    public static void loadDictionary(){


        String projectDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        String dataDirectory = projectDirectory+"/resources/sortedposnegword.txt";
        long startTime,endTime;
        try {
            startTime = System.currentTimeMillis();
            Files.newBufferedReader(Paths.get(dataDirectory)).lines().forEach(item->{
                String[] splittedItem = item.split(",");
                wordDictionary.put(splittedItem[0], Double.valueOf(splittedItem[1]));
            });
            endTime = System.currentTimeMillis();
            System.out.println("Dictionary Loaded Successfully.");
            System.out.println("Elapsed time to load dictionary "+(endTime-startTime)+" milliseconds.");
        } catch (IOException e) {
            System.out.println("Error Reading Data Dictionary From File.");
        }
        System.out.println("Total Number Of Words in Dictionary "+wordDictionary.size());
    }

    public static Hashtable<String, Double> getWordDictionary() {
        return wordDictionary;
    }

    public static void setWordDictionary(Hashtable<String, Double> wordDictionary) {
        DataDictionary.wordDictionary = wordDictionary;
    }
}

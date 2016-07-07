package Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Author: SACHIN
 * Date: 7/6/2016.
 */
public class DataDictionary {


    public static Hashtable<String,Double> wordDictionary = new Hashtable<>();
    public static List<String> wordsTobeIgnored = new ArrayList<>(Arrays.asList("the","is","a","an"));


    public static void loadDictionary(){

    }

    public static Hashtable<String, Double> getWordDictionary() {
        return wordDictionary;
    }

    public static void setWordDictionary(Hashtable<String, Double> wordDictionary) {
        DataDictionary.wordDictionary = wordDictionary;
    }
}

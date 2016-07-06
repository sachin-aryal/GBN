package Service;

import java.util.Hashtable;

/**
 * Author: SACHIN
 * Date: 7/6/2016.
 */
public class DataDictionary {


    private static Hashtable<String,Double> wordDictionary = new Hashtable<>();

    public static void loadDictionary(){

    }

    public static Hashtable<String, Double> getWordDictionary() {
        return wordDictionary;
    }

    public static void setWordDictionary(Hashtable<String, Double> wordDictionary) {
        DataDictionary.wordDictionary = wordDictionary;
    }
}

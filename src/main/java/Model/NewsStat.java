package Model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iam on 7/7/16.
 */
public class NewsStat {
    public int noOfNews;
    public Map<String,String> selectedNews = new LinkedHashMap<>();
    public Map<String,Double> newsPolarity = new LinkedHashMap<>();
}

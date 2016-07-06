package Service;

import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by iam on 7/6/16.
 */
public class Crawler{


    public static void main(String[] args) {
        try {
            new Crawler().getNewsData("good",5);
        } catch (IOException e) {
            System.out.println("Remote Connection Failed.");
        }
    }

    private String himalyanTimesUrl = "https://thehimalayantimes.com/category/nepal/";

    public JsonObject getNewsData(String classifier,int numberOfNews) throws IOException {
        JsonObject news = new JsonObject();
        Document doc = Jsoup.connect(himalyanTimesUrl).get();
        System.out.println("Connected Successfully");
        Element mainNews = doc.getElementById("mainNews");
        Elements aTag = mainNews.getElementsByTag("a");
        System.out.printf(String.valueOf(aTag.get(0)));

        return news;
    }

}

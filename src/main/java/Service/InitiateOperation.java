package Service;

import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * Created by iam on 7/7/16.
 */
public class InitiateOperation {
    private int noOfNews;
    private String newsType;


    public InitiateOperation(int noOfNews,String newsType){
        this.noOfNews = noOfNews;
        this.newsType = newsType;
    }

    public JsonObject getNewsData(){
        Crawler crawler = new Crawler();
        try {
            System.out.println("Initiating Application");
            return crawler.getNewsData(newsType,noOfNews);
        } catch (IOException e) {
            System.out.println("Error Fetching Data From Remote");
        }
        return null;
    }

}

package Service;

import io.vertx.core.json.JsonObject;

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
        JsonObject requiredNews = new JsonObject();


        return requiredNews;
    }

}

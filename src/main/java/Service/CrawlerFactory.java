package Service;

import Model.Crawler;
import Service.BBCNews;
import Service.KathmanduPost;

/**
 * Created by anons on 7/19/16.
 */
public class CrawlerFactory {
    public static Crawler getCrawler(String crawlerType){
        if (crawlerType==null){
            return null;
        }
        if (crawlerType.equalsIgnoreCase("KathmanduPost")){
            return new KathmanduPost();
        }
        else if (crawlerType.equalsIgnoreCase("BBCNews")){
            return new BBCNews();
        }
        else if (crawlerType.equalsIgnoreCase("MyRepublica")){
            //return new MyRepublica();
        }
        else if (crawlerType.equalsIgnoreCase("FoxNews")){
            return new FoxNews();
        }
        else{
            return new KathmanduPost();
        }
        return null;
    }
}

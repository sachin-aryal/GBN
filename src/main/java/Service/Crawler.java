package Service;

import Model.News;
import Model.NewsStat;
import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by iam on 7/6/16.
 */
public class Crawler{


    private String kathmanduPostUrl = "http://kathmandupost.ekantipur.com/category/national";
    private String newsURL = "http://kathmandupost.ekantipur.com";

    public JsonObject getNewsData(String classifier ,int numberOfNews) throws IOException {

        JsonObject news = new JsonObject();
        List<News> newsList = getNewsList(classifier,numberOfNews);
        for(News n:newsList){
            System.out.println("Title: "+n.getTitle());
            System.out.println("Description: "+n.getDescription());
            System.out.println();
        }
        return news;
    }

    public List<News> getNewsList(String newsType,int numberOfNews) throws IOException {

        List<News> newsList = new ArrayList<>();
        Document doc = Jsoup.connect(kathmanduPostUrl).get();
        System.out.println("Connected to Remote URL");

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Elements mainNews = doc.select(".category-news-list");
        Elements newsWrapper = mainNews.select(".wrap");
        Elements links = newsWrapper.select("a[href]");

        NewsStat newsStat = new NewsStat();

        links.forEach(link->{
            String newsUrl = link.attr("href");
            String newsTitle =link.text();

            if(newsUrl.contains(".html")){
                News news = new News();
                news.setTitle(newsTitle);

                try {
                    news.setDescription(getUrlContent(newsUrl));
                    //Changed below condition
                    if(numberOfNews>newsStat.noOfNews){
                        executorService.execute(new PolarityCalculator(newsType,news,newsStat));
                    }

                } catch (IOException e) {
                    System.out.println("Error Fetching Data from "+newsUrl);
                }
            }
        });
        executorService.shutdown();

        newsStat.selectedNews.forEach((key,val)->newsList.add(new News(key,val)));

        return newsList;
    }


    public String getUrlContent(String url) throws IOException {

        String finalUrl = newsURL+url;
        Document doc =  Jsoup.connect(finalUrl).get();
        Elements newsContent = doc.select(".content-wrapper");
        Elements pTags = newsContent.select("p");
        pTags.remove(pTags.size()-1);
        StringBuilder fullNews = new StringBuilder("");
        pTags.forEach(p->fullNews.append(p.text()));
        return fullNews.toString();
    }


}

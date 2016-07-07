package Service;

import Model.News;
import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private String kathmanduPostUrl = "http://kathmandupost.ekantipur.com/category/national";
    private String newsURL = "http://kathmandupost.ekantipur.com";

    public JsonObject getNewsData(String classifier ,int numberOfNews) throws IOException {
        JsonObject news = new JsonObject();
        List<News> newsList = getNewsList(numberOfNews);
        for(News n:newsList){
            System.out.println("Title: "+n.getTitle());
            System.out.println("Description: "+n.getDescription());
            System.out.println();
        }
        return news;
    }

    public List<News> getNewsList(int numberOfNews) throws IOException {
        List<News> newsList = new ArrayList<News>();

        Document doc = Jsoup.connect(kathmanduPostUrl).get();

        System.out.println("Connected to Remote URL");

        Elements mainNews = doc.select(".category-news-list");
        Elements newsWrapper = mainNews.select(".wrap");
        Elements links = newsWrapper.select("a[href]");
        links.forEach(link->{
            if(newsList.size()<numberOfNews){

                String newsUrl = link.attr("href");
                String newsTitle =link.text();

                if(newsUrl.contains(".html")){
                    News news = new News();
                    news.setTitle(newsTitle);
                    try {
                        news.setDescription(getUrlContent(newsUrl));
                        newsList.add(news);
                    } catch (IOException e) {
                        System.out.println("Error Fetching Data from "+newsUrl);
                    }
                }
            }
        });
        return newsList;
    }


    public String getUrlContent(String url) throws IOException {

        String finalUrl = newsURL+url;
        System.out.println("finalUrl = " + finalUrl);
        Document doc =  Jsoup.connect(finalUrl).get();
        Elements newsContent = doc.select(".content-wrapper");
        Elements pTags = newsContent.select("p");
        pTags.remove(pTags.size()-1);
        StringBuilder fullNews = new StringBuilder("");
        pTags.forEach(p->fullNews.append(p.text()));
        return fullNews.toString();
    }


    public List<String> getTokenizedWords(String content){
        List<String> tokenizationCompleted = new ArrayList<>();
        String[] tokenizedWords = content.split(" ");
        for (String word:tokenizedWords){
            word = word.trim().toLowerCase();
            if(word.contains(",")){
                String[] splittedWords = word.split(",");
                tokenizationCompleted.add(splittedWords[0].replaceAll("[^a-z]",""));
                tokenizationCompleted.add(splittedWords[1].replaceAll("[^a-z]",""));
            }else{
                word = word.replaceAll("[^a-zA-Z]","");
                tokenizationCompleted.add(word);
            }
        }

        return tokenizationCompleted;
    }

}

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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by iam on 7/6/16.
 */
public class Crawler{


    private String kathmanduPostUrl = "http://kathmandupost.ekantipur.com/category/national";
    private String newsURL = "http://kathmandupost.ekantipur.com";

    public JsonObject getNewsData(String classifier ,int numberOfNews) throws IOException {

        JsonObject news = new JsonObject();
        List<News> newsList = getNewsList(classifier,numberOfNews);
        newsList.forEach(n->news.put(n.getTitle(),n.getDescription()));
        System.out.println("Total Number of News Returned: "+newsList.size());
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
        System.out.println("Total Number of News Crawled are: "+links.size());
        System.out.println("Total Usable Links are: "+ links.stream().filter(link->link.attr("href").contains(".html"))
                .collect(Collectors.toList()).size());
        links
                .stream()
                .filter(link->link.attr("href").contains(".html"))
                .collect(Collectors.toList())
                .forEach(link->{

                    String newsUrl = link.attr("href");
                    String newsTitle =link.text();
                    News news = new News();
                    news.setTitle(newsTitle);

                    try {
                        news.setDescription(getUrlContent(newsUrl));
                        executorService.execute(new PolarityCalculator(newsType,news,newsStat));
                    } catch (IOException e) {
                        System.out.println("Error Fetching Data from "+newsUrl);
                    }
                });
        executorService.shutdown();
        setBestResult(newsList,newsStat,newsType,numberOfNews);
        return newsList;
    }

    public void setBestResult(List<News> newsList,NewsStat newsStat,String newsType,int noOfNews){
        List<String> keys = null;
        if(newsType.equalsIgnoreCase("good")){
            keys = newsStat.newsPolarity.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(noOfNews)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }else{
            System.out.println(newsStat.newsPolarity);
            keys = newsStat.newsPolarity.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue())
                    .limit(noOfNews)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        keys.forEach(key->newsList.add(new News(key,newsStat.selectedNews.get(key))));

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

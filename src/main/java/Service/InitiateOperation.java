package Service;

import Model.Crawler;
import Model.News;
import Model.NewsStat;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by iam on 7/7/16.
 */
public class InitiateOperation {
    private int noOfNews;
    private String newsType;
    private String newsSource;


    public InitiateOperation(int noOfNews,String newsType, String newsSource){
        this.noOfNews = noOfNews;
        this.newsType = newsType;
        this.newsSource = newsSource;
    }

    public JsonObject getNewsData(){
        System.out.println(newsSource);
        Crawler crawler = CrawlerFactory.getCrawler(newsSource);
        try {
            System.out.println("Initiating Application");
            List<News> newsToDisplay = new ArrayList<>();
            JsonObject news = new JsonObject();
            List<News> newsList = crawler.getNewsList();

            ExecutorService executorService = Executors.newFixedThreadPool(5);
            NewsStat newsStat = new NewsStat();

            System.out.println("Calculating Polarity");
            newsList.forEach(link->{
//                System.out.println(link);
                executorService.execute(new PolarityCalculator(newsType,link,newsStat));
            });

            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                System.out.println("All Task Completed.");
            } catch (InterruptedException e) {
                System.out.println("Await Termination.");
            }

            setBestResult(newsToDisplay,newsStat,newsType,noOfNews);

            newsToDisplay.forEach(n->news.put(n.getTitle(),n.getDescription()));
//            System.out.println("Total Number of News Returned: "+newsList.size());
            return news;
        } catch (IOException e) {
            System.out.println("Error Fetching Data From Remote");
        }
        return null;
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

}

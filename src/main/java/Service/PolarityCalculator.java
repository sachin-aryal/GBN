package Service;

import Model.News;
import Model.NewsStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iam on 7/6/16.
 */
public class PolarityCalculator extends Thread{

    private String newsType;
    private News news;
    private NewsStat newsStat;

    public PolarityCalculator(String newsType, News news, NewsStat newsStat){
        this.newsType = newsType;
        this.news = news;
        this.newsStat = newsStat;
    }

    @Override
    public void run(){

        double polarity = getPolarity(news);

        if(newsType.equalsIgnoreCase("good")){
            if(polarity>0.0){
                newsStat.noOfNews++;
                newsStat.selectedNews.put(news.getTitle(),news.getDescription());
                newsStat.newsPolarity.put(news.getTitle(),polarity);
            }
        }else{
            if(polarity<0.0){
                newsStat.noOfNews++;
                newsStat.selectedNews.put(news.getTitle(),news.getDescription());
                newsStat.newsPolarity.put(news.getTitle(),polarity);
            }
        }

    }



    public double getPolarity(News news){

        double polarity = 0.0;
        List<String> tokenizedTitle = getTokenizedWords(news.getTitle());
        List<String> tokenizedContent = getTokenizedWords(news.getDescription());

        //// TODO: 7/7/16 Calculating Polarity for News Title and Content
        for (String item:tokenizedTitle) {
            if (DataDictionary.wordDictionary.get(item)!=null){
                polarity+= DataDictionary.wordDictionary.get(item);
            }
        }
        for (String item:tokenizedContent){
            if (DataDictionary.wordDictionary.get(item)!=null){
                polarity+= DataDictionary.wordDictionary.get(item);
            }
        }
        return polarity;
    }



    public List<String> getTokenizedWords(String content){
        List<String> tokenizationCompleted = new ArrayList<>();
        String[] tokenizedWords = content.split(" ");
        for (String word:tokenizedWords){
            word = word.trim().toLowerCase();
            if(word.contains(",")){
                String[] splittedWords = word.split(",");
                tokenizationCompleted.add(splittedWords[0].replaceAll("[^a-z]",""));
            }else{
                word = word.replaceAll("[^a-zA-Z]","");
                tokenizationCompleted.add(word);
            }
        }
        return tokenizationCompleted;
    }
}

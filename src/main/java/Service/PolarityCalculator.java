package Service;

import Model.News;
import Model.NewsStat;
import edu.stanford.nlp.process.Stemmer;

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
        Stemmer s = new Stemmer();

        //// TODO: 7/7/16 Calculating Polarity for News Title and Content
        double titlePolarity = calculatePolarity(tokenizedTitle);
        /*int itemIndex = 0;
        for (String item:tokenizedTitle) {
            String stemmeredWord = s.stem(item);
            if (!DataDictionary.wordsTobeIgnored.contains(stemmeredWord)){
                double tempPolarity = DataDictionary.wordDictionary.get(stemmeredWord);
                if(itemIndex>0){
                    String previousWord = tokenizedTitle.get(itemIndex-1);
                    if(DataDictionary.negationWord.contains(previousWord)){
                        if(tempPolarity<0){
                            tempPolarity = Math.abs(tempPolarity);
                        }else{
                            tempPolarity = tempPolarity*-1;
                        }
                    }
                }
                System.out.println("Key Word: "+stemmeredWord+" and Polarity: "+tempPolarity);
                titlePolarity+= tempPolarity;
            }
            itemIndex++;


        }*/
        System.out.println("News Title: "+news.getTitle()+" and Polarity: "+titlePolarity);

        /*for (String item:tokenizedContent) {
            String stemmeredWord = s.stem(item);
            if (DataDictionary.wordDictionary.get(stemmeredWord) != null) {
                double tempPolarity = DataDictionary.wordDictionary.get(stemmeredWord);
                if (itemIndex > 0) {
                    String previousWord = tokenizedTitle.get(itemIndex - 1);
                    if (DataDictionary.negationWord.contains(previousWord)) {
                        if (tempPolarity < 0) {
                            tempPolarity = Math.abs(tempPolarity);
                        } else {
                            tempPolarity = tempPolarity * -1;
                        }
                    }
                }
                contentPolarity += tempPolarity;
            }
        }*/
        double contentPolarity = calculatePolarity(tokenizedContent);

        System.out.println("After Content Polarity is "+contentPolarity);

        polarity = ((titlePolarity*6)/10)+((contentPolarity*4)/10);

        System.out.println("Final Polarity: "+polarity);

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

    public double calculatePolarity(List<String> tokenizedWord){
        Stemmer s = new Stemmer();
        double polarity=0.0;
        int itemIndex = 0;
        for (String item:tokenizedWord) {
            String stemmeredWord = s.stem(item);
            if (!DataDictionary.wordsTobeIgnored.contains(stemmeredWord)){
                if (DataDictionary.wordDictionary.get(stemmeredWord) != null) {
                    double tempPolarity = DataDictionary.wordDictionary.get(stemmeredWord);
                    if (itemIndex > 0) {
                        String previousWord = tokenizedWord.get(itemIndex - 1);
                        if (DataDictionary.negationWord.contains(previousWord)) {
                            if (tempPolarity < 0) {
                                tempPolarity = Math.abs(tempPolarity);
                            } else {
                                tempPolarity = tempPolarity * -1;
                            }
                        }
                    }
                    System.out.println("Key Word: " + stemmeredWord + " and Polarity: " + tempPolarity);
                    polarity += tempPolarity;
                }
            }
            itemIndex++;
        }
        return polarity;
    }
}

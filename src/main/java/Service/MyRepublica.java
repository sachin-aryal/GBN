package Service;

import Model.Crawler;
import Model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anons on 7/19/16.
 */
public class MyRepublica implements Crawler {

    private String myrepublicaUrl = "http://www.myrepublica.com/category/24";
    private String newsURL = "http://www.myrepublica.com";

    /*    public JsonObject getNewsData() throws IOException {
            JsonObject news = new JsonObject();
            List<News> newsList = getNewsList();
            newsList.forEach(n->news.put(n.getTitle(),n.getDescription()));
            System.out.println("Total Number of News Returned: "+newsList.size());
            return news;
        }*/
    @Override
    public List<News> getNewsList() throws IOException {

        List<News> newsList = new ArrayList<>();
        Document doc = Jsoup.connect(myrepublicaUrl).get();
        //System.out.println("doc = " + doc);
        //System.out.println("Connected to Remote URL");

//        Elements mainNews = doc.select(".container");
        //System.out.println("mainNews = " + mainNews);
        Elements newsWrapper = doc.select(".main-heading");
        //System.out.println("newsWrapper = " + newsWrapper);
        Elements links = newsWrapper.select("a[href]");

        System.out.println("Total Number of News Crawled are: "+links.size());
        links
                .stream()
                .filter(link -> !link.attr("href").equals("/news/"))
                .collect(Collectors.toList())
                .forEach(link->{

                    String newsUrl = link.attr("href");
                    //System.out.println("newsUrl = " + newsUrl);
                    String newsTitle =link.text();
                    News news = new News();
                    news.setTitle(newsTitle);
                    try {
                        news.setDescription(getUrlContent(newsUrl));
                    } catch (IOException e) {
                        System.out.println("Error Fetching Data from "+newsUrl);
                    }
                    newsList.add(news);
                });
        return newsList;
    }

    public String getUrlContent(String url) throws IOException {
        StringBuilder fullNews = new StringBuilder("");
        try {
            String finalUrl = newsURL+url;
            Document doc =  Jsoup.connect(finalUrl).get();
            Elements main = doc.select(".recent-news-categories-details");
            Elements pTag = main.select("p");
            List<Integer> newsList = new ArrayList<>();
            pTag.forEach(p->{
                newsList.add(p.text().length());
            });

            int max = Collections.max(newsList);
            return pTag.get(newsList.indexOf(max)).text();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return fullNews.toString();
    }
}

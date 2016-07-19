package Service;

import Model.Crawler;
import Model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anons on 7/19/16.
 */
public class KathmanduPost implements Crawler {

    private String kathmanduPostUrl = "http://kathmandupost.ekantipur.com/category/national";
    private String newsURL = "http://kathmandupost.ekantipur.com";

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
        Document doc = Jsoup.connect(kathmanduPostUrl).get();
        System.out.println("Connected to Remote URL");

        Elements mainNews = doc.select(".category-news-list");
        Elements newsWrapper = mainNews.select(".wrap");
        Elements links = newsWrapper.select("a[href]");

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
                    } catch (IOException e) {
                        System.out.println("Error Fetching Data from "+newsUrl);
                    }
                    newsList.add(news);
                });
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

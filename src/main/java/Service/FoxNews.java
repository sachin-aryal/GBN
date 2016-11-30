package Service;

import Model.Crawler;
import Model.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 11/30/2016.
 */
public class FoxNews implements Crawler{
    private static String fnPostUrl = "http://www.foxnews.com/science.html";
    private static String scienceNewsURL = "http://www.foxnews.com/";

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
        System.out.println(fnPostUrl);
        Document doc = Jsoup.connect(fnPostUrl).get();
        System.out.println("Connected to Remote URL");


        Elements mainNews = doc.select(".headline");
//        Elements newsWrapper = mainNews.select(".wrap");
        Elements links = mainNews.select("a[href]");



        System.out.println("Total Number of News Crawled are: "+links.size());
        links.forEach(link->{
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

    public static String getUrlContent(String url) throws IOException {

        String finalUrl = scienceNewsURL+url;
        Document doc =  Jsoup.connect(finalUrl).get();
        Elements newsContent = doc.select(".article-text");
        Elements pTags = newsContent.select("p");
        StringBuilder fullNews = new StringBuilder("");
        pTags.forEach(p->fullNews.append(p.text()));
        return fullNews.toString();
    }
}

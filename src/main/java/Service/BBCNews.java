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
 * Created by anons on 7/19/16.
 */
public class BBCNews implements Crawler {

    private static String bbcPostUrl = "http://www.bbc.com/news/";
    private static String newsURL = "http://www.bbc.com";

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
        List<Element> validLinks = new ArrayList<Element>();
        Document doc = Jsoup.connect(bbcPostUrl).get();
        System.out.println("Connected to Remote URL");


        Elements mainNews = doc.select(".column--primary");
        Elements links = mainNews.select(".title-link");

        System.out.println("Total Number of News Crawled are: "+links.size());
        links.forEach(link->{
            if (!link.attr("href").contains("video")&&!link.attr("href").contains("sport")){
                validLinks.add(link);
                Elements e    = link.select("span");
                e.forEach(sp->{
                    if(sp.text().contains("Video")){
                        validLinks.remove(link);
                    }
                });
            }
        });
        System.out.println("Total Usable Links are: "+validLinks.size());
        validLinks.forEach(link->{
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

        String finalUrl = newsURL+url;
        Document doc =  Jsoup.connect(finalUrl).get();
        Elements newsContent = doc.select(".story-body__inner");
        Elements pTags = newsContent.select("p");
        StringBuilder fullNews = new StringBuilder("");
        pTags.forEach(p->fullNews.append(p.text()));
        return fullNews.toString();
    }
}

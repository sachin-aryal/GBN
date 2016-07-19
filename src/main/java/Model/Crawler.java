package Model;

import Model.News;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by anons on 7/19/16.
 */
public interface Crawler {
    public List<News> getNewsList() throws IOException;
}

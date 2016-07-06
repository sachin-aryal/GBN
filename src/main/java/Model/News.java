package Model;

/**
 * Created by iam on 7/6/16.
 */
public class News{

    String title;
    String description;

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public News(String title, String description) {
        this.title = title;
        this.description = description;
    }

}

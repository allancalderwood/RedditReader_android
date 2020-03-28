package redditreader.com.redditreader_android.models;

public class Subreddit {
    String name;
    String iconURL;
    String headerURL;

    public Subreddit(String name, String iconURL, String headerURL) {
        this.name = name;
        this.iconURL = iconURL;
        this.headerURL = headerURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getHeaderURL() {
        return headerURL;
    }

    public void setHeaderURL(String headerURL) {
        this.headerURL = headerURL;
    }
}

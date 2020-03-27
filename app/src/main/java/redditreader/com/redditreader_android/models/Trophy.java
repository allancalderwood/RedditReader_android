package redditreader.com.redditreader_android.models;

public class Trophy {
    private String name;
    private String iconURL;

    public Trophy(String name, String iconURL) {
        this.name = name;
        this.iconURL = iconURL;
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
}

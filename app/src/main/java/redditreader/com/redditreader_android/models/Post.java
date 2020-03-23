package redditreader.com.redditreader_android.models;

public class Post {
    private String id;
    private String subreddit;
    private String title;
    private String selftext;
    private int score;
    private String authorName;
    private String authorID;
    private String imageURL;
    private int imageWidth;
    private int imageHeight;
    private String imageURLPreview;
    private String time;
    private int numComments;
    private String url;
    private int numAwards;
    private String mediaURL;
    private boolean media = false;

    public Post(String id, String authorID, String authorName,
      String imageURL, String imageURLPreview,
      String title, String selftext, String subreddit,
      int score, int numComments, String time, String url, int numAwards){
        this.id = id;
        this.authorID= authorID;
        this.authorName= authorName;
        this.imageURL= imageURL;
        this.imageURLPreview= imageURLPreview;
        this.title=title;
        this.selftext=selftext;
        this.subreddit=subreddit;
        this.score=score;
        this.numComments=numComments;
        this.time=time;
        this.url= "www.reddit.com/"+url;
        this.numAwards=numAwards;
    }

    public Post(String id, String authorID, String authorName,
         String imageURL, String imageURLPreview, int imageHeight, int imageWidth,
         String title, String selftext, String subreddit,
         int score, int numComments, String time, String url, int numAwards){
        this.id = id;
        this.authorID= authorID;
        this.authorName= authorName;
        this.imageURL= imageURL;
        this.imageURLPreview= imageURLPreview;
        this.title=title;
        this.selftext=selftext;
        this.subreddit=subreddit;
        this.score=score;
        this.numComments=numComments;
        this.time=time;
        this.url= "www.reddit.com/"+url;
        this.numAwards=numAwards;
        this.imageHeight=imageHeight;
        this.imageWidth=imageWidth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageURLPreview() {
        return imageURLPreview;
    }

    public void setImageURLPreview(String imageURLPreview) {
        this.imageURLPreview = imageURLPreview;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumAwards() {
        return numAwards;
    }

    public void setNumAwards(int numAwards) {
        this.numAwards = numAwards;
    }
}

package redditreader.com.redditreader_android.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.models.Post;

import static redditreader.com.redditreader_android.utils.TimestampHelper.readTimestamp;

public class PostFactory {

    public static void postFact(JSONObject jsonData, ArrayList<Post> posts){

        try {
            JSONArray arr = jsonData.getJSONObject("data").getJSONArray("children");
            for(int i=0; i<arr.length();i++){
                JSONObject p = arr.getJSONObject(i).getJSONObject("data");
                String time = readTimestamp(p.getInt("created_utc"));
                Post post;
                if(!(p.has("preview"))){
                    post = new Post(p.getString("name"),p.getString("author_fullname"), p.getString("author"),
                            p.getString("url"), p.getString("thumbnail"),
                            p.getString("title"), p.getString("selftext"), p.getString("subreddit"),
                            p.getInt("score"), p.getInt("num_comments"), time, p.getString("permalink"),p.getInt("total_awards_received"));
                }else{
                    post = new Post(p.getString("name"),p.getString("author_fullname"), p.getString("author"),
                            p.getString("url"), p.getString("thumbnail"),
                            p.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getInt("height"),
                            p.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getInt("width"),
                            p.getString("title"), p.getString("selftext"), p.getString("subreddit"),
                            p.getInt("score"), p.getInt("num_comments"), time, p.getString("permalink"),p.getInt("total_awards_received"));
                }
                posts.add(post);
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }

    }

}

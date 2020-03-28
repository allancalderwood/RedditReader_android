package redditreader.com.redditreader_android.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.Subreddit;

import static redditreader.com.redditreader_android.utils.TimestampHelper.readTimestamp;

public class SubFactory {

    public static void subFact(JSONObject jsonData, ArrayList<Subreddit> subs){

        try {
            JSONArray arr = jsonData.getJSONObject("data").getJSONArray("children");
            for(int i=0; i<arr.length();i++){
                JSONObject p = arr.getJSONObject(i).getJSONObject("data");
                Subreddit sub = new Subreddit(p.getString("display_name"),p.getString("icon_img"),p.getString("header_img"));
                subs.add(sub);
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }

    }

}

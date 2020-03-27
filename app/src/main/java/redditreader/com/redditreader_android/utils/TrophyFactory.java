package redditreader.com.redditreader_android.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.models.Trophy;

public class TrophyFactory {

    public static void trophyFact(JSONObject jsonData, ArrayList<Trophy> trophies){
        try {
            JSONArray arr = jsonData.getJSONObject("data").getJSONArray("trophies");
            for(int i=0; i<arr.length();i++){
                JSONObject p = arr.getJSONObject(i).getJSONObject("data");
                Trophy trophy;
                trophy = new Trophy(p.getString("name"),p.getString("icon_70"));
                trophies.add(trophy);
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }

    }

}

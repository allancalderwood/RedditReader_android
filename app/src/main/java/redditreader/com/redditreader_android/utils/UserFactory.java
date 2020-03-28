package redditreader.com.redditreader_android.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.models.Trophy;
import redditreader.com.redditreader_android.models.UserOther;

public class UserFactory {

    public static void userFact(JSONObject jsonData, ArrayList<UserOther> users){
        try {
            JSONArray arr = jsonData.getJSONObject("data").getJSONArray("children");
            for(int i=0; i<arr.length();i++){
                JSONObject p = arr.getJSONObject(i).getJSONObject("data");
                UserOther user;
                user = new UserOther(p.getString("name"),p.getString("icon_img"));
                users.add(user);
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }

    }

}

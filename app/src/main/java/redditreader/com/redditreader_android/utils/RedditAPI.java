package redditreader.com.redditreader_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.function.Function;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.models.User;

import static android.content.Context.MODE_PRIVATE;

public class RedditAPI {
    final  static String authBaseURL = "https://www.reddit.com/api/v1/authorize.compact?";
    final  static String _clientID = "q0_ONlYdebK_MQ";
    final  static String redirectURI = "https://www.reddit.com/user/AllanCalderwood/";
    final  static String duration = "permanent";
    final  static String _scope = "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread";
    final  static String tokenBaseURL = "https://www.reddit.com/api/v1/access_token";
    final  static String _userAgent = "android:com.redditreader.redditreader_android:v.1 (by /u/AllanCalderwood)";
    final  static String credentials = Base64.getEncoder().encodeToString((_clientID+":").getBytes());
    final  static String callBaseURL = "https://oauth.reddit.com/";

    private static HttpURLConnection connection;

    public static String getCallBaseURL() {
        return callBaseURL;
    }
    public static String getRedirectURI() {
        return redirectURI;
    }

    public  String authorizeURL(){
        String _state = _generateStateString();
        String _url = authBaseURL +
        "client_id=" + _clientID + "&" +
        "response_type=code&" +
        "state=" + _state + "&" +
        "redirect_uri=" + redirectURI + "&" +
        "duration=" + duration + "&" +
        "scope=" + _scope;

        return _url;
    }

    private String _generateStateString(){
        final String _symbols =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // allowed symbols

        String _state = "";

        for(int i=0; i<8; i++){
            _state+=_symbols.charAt((int)( Math.random()*_symbols.length() ));
        }

        return _state;
    }

    public static void getAccessToken(String codes){
        final SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String fields =
                "grant_type=authorization_code&"
                +"code="+codes+"&"+
                "redirect_uri="+redirectURI;
        PostRequest pr = new PostRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    sharedPreferences.edit().putString("access_token",jo.getString("access_token")).apply();
                    sharedPreferences.edit().putString("refresh_token",jo.getString("refresh_token")).apply();
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        pr.execute(tokenBaseURL, fields, "Basic");
        updateUser(sharedPreferences);
    }

    public static void refreshToken(){
        SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String refreshToken = sharedPreferences.getString("refresh_token","");
        String fields =
                "grant_type=refresh_token&"
                        +"refresh_token="+refreshToken;
        PostRequest pr = new PostRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    // TODO store tokens locally
                    SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putString("access_token",jo.getString("access_token")).apply();
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        pr.execute(tokenBaseURL, fields, "Basic");
        updateUser(sharedPreferences);
    }

    public static void refreshToken(final Context context){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String refreshToken = sharedPreferences.getString("refresh_token","");
        String fields =
                "grant_type=refresh_token&"
                        +"refresh_token="+refreshToken;
        PostRequest pr = new PostRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    sharedPreferences.edit().putString("access_token",jo.getString("access_token")).apply();
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        pr.execute(tokenBaseURL, fields, "Basic");
        updateUser(sharedPreferences);
    }

    public static void updateUser(SharedPreferences sharedPreferences){
        // Get user details
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    User.setUsername(jo.getString("name"));
                    User.setKarma(((jo.getInt("link_karma") + jo.getInt("comment_karma") )));
                    long seconds = jo.getLong("created")/10;
                    Long daysSinceCreated = seconds/86400;
                    Double age;
                    String message = " days old";


                    if(daysSinceCreated>30 && daysSinceCreated<365){
                        daysSinceCreated =  daysSinceCreated/30;
                        age = Math.floor(daysSinceCreated.doubleValue());
                        message = " mnths old";
                    }
                    else if(daysSinceCreated>365){
                        daysSinceCreated =  daysSinceCreated/365;
                        age = Math.floor(daysSinceCreated.doubleValue());
                        message = " yr old";
                    }else{
                        age = daysSinceCreated.doubleValue();
                    }
                    User.setAccountAge((age.intValue()));
                    User.setAccountAgePostfix(message);
                    User.setProfileURL(jo.getString("icon_img"));
                    User.storeUser();
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = sharedPreferences.getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"api/v1/me", "Bearer", auth);
    }
}

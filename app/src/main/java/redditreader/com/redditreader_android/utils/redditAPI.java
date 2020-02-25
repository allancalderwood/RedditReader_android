package redditreader.com.redditreader_android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.Base64;

import redditreader.com.redditreader_android.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class redditAPI {
    final  static String authBaseURL = "https://www.reddit.com/api/v1/authorize.compact?";
    final  static String _clientID = "q0_ONlYdebK_MQ";
    final  static String redirectURI = "https://www.reddit.com/user/AllanCalderwood/";
    final  static String duration = "permanent";
    final  static String _scope = "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread";
    final  static String tokenBaseURL = "https://www.reddit.com/api/v1/access_token";
    final  static String _userAgent = "android:com.redditreader.redditreader_android:v.1 (by /u/AllanCalderwood)";
    final  static String credentials = Base64.getEncoder().encodeToString((_clientID+":").getBytes());

    private static HttpURLConnection connection;

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

    public static void getAccessToken(String codes, Context context){
        final Context c = context;
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
                    // TODO store tokens locally
                    SharedPreferences sharedPreferences = c.getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                    sharedPreferences.edit().putString("access_token",jo.getString("access_token")).apply();
                    sharedPreferences.edit().putString("refresh_token",jo.getString("refresh_token")).apply();
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        pr.execute(tokenBaseURL, fields);
    }
}

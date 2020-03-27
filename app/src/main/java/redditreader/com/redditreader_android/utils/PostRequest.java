package redditreader.com.redditreader_android.utils;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/* PARAMS 0 is the URL
   PARAMS 1 is the Message body
   PARAMS 2 is the Auth Type, Basic for no log in and Bearer for logged in
   PARAMS 3 is the access token if logged in
 */


public class PostRequest extends AsyncTask<String, String, String[]>{
    private String METHOD = "POST";
    private String TYPE = "application/x-www-form-urlencoded";
    private String USER_AGENT = RedditAPI._userAgent;
    private String CREDENTIALS = RedditAPI.credentials;
    private AsyncResponse delegate = null;

    public PostRequest(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }


    protected String[] doInBackground(String... params){
        HttpsURLConnection connection = null;
        String response[] = {"404","Error"};

        try {
            URL url = new URL(params[0]);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD);
            if (params[2].equals("Basic")){
                connection.setRequestProperty("Authorization","Basic "+CREDENTIALS);
            }else if(params[2].equals("Bearer")){
                connection.setRequestProperty("Authorization","Bearer "+params[3]);
            }else if(params[2].equals("Custom")){
                connection.setRequestProperty("Authorization", params[3]);
            }
            connection.addRequestProperty("User-Agent", USER_AGENT);
            connection.addRequestProperty("Content-Type", TYPE);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(params[1]);
            dos.flush();
            dos.close();

            InputStream is = null;

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                is = connection.getInputStream();
            }
            else {
                is = connection.getErrorStream();
            }



            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                is.close();
                response[0] = String.valueOf(connection.getResponseCode());
                response[1] = sb.toString();

            }catch (Exception e){
                Log.e("BUFFER ERROR", "Error converting result " + e.toString());
            }

            connection.disconnect();
            return response;

        } catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String[] result){
        delegate.processFinish(result);
    }

}

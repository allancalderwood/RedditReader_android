package redditreader.com.redditreader_android.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.utils.redditAPI;

public class AuthActivity extends AppCompatActivity{
    private WebView webView;
    private WebViewClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_auth);

        webView = findViewById(R.id.webView);

        client = new WebViewClient(){
            @Override
            public void onPageStarted(WebView view,String url, Bitmap favicon) {
                if(url.startsWith(redditAPI.getRedirectURI())){
                    if(url.contains("access_denied")){
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(intent);
                        }
                    else {
                        String _code = url.substring( // Retrieve the authorization code
                                url.indexOf("code=")+5
                                );

                        // retrieve access token and refresh token and store them
                        redditAPI.getAccessToken(_code, getApplicationContext());
                        getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);

                       // getAccessToken(_code).then((value) => writeTokensStorage(value));
                    }
                }
                super.onPageStarted(view, url, favicon);
            }
        };

        webView.setWebViewClient(client);
        webView.loadUrl(new redditAPI().authorizeURL());

    }

}
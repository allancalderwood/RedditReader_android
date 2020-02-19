package redditreader.com.redditreader_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    private WebView webView;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

       // buttonBack = findViewById(R.id.buttonBack);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.reddit.com/register/");

       /* buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View v){
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                startActivity(intent);
            }
         });*/
    }

}

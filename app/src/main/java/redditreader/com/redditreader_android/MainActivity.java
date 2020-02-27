package redditreader.com.redditreader_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.screens.AuthActivity;
import redditreader.com.redditreader_android.screens.HomepageActivity;
import redditreader.com.redditreader_android.screens.RegisterActivity;
import redditreader.com.redditreader_android.utils.RedditAPI;


public class MainActivity extends AppCompatActivity {
    private Button buttonLogin;
    private Button buttonRegister;
    public static final String SHARED_PREFS = "RedditReaderSharedPrefs";
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        String auth = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        if( !(auth.equals(""))) { //if logged in
            User.retrieveUser();
            RedditAPI.refreshToken(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_login);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);


            buttonLogin = findViewById(R.id.buttonLogin);
            buttonRegister = findViewById(R.id.buttonRegister);

            buttonLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(v.getContext(), AuthActivity.class);
                    startActivity(intent);
                }
            });

            buttonRegister.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

}

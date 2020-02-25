package redditreader.com.redditreader_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import redditreader.com.redditreader_android.screens.AuthActivity;
import redditreader.com.redditreader_android.screens.RegisterActivity;


public class MainActivity extends AppCompatActivity {
    private Button buttonLogin;
    private Button buttonRegister;
    public static final String SHARED_PREFS = "RedditReaderSharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String auth = sharedPreferences.getString("access_token","");
        if( !(auth.equals(""))) { //if logged in
            // TODO Homepage
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

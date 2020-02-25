package redditreader.com.redditreader_android.screens;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.DownloadImageTask;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomepageActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        drawerView = findViewById(R.id.drawerView);
        View headerView = drawerView.getHeaderView(0);
        final TextView usernameText = headerView.findViewById(R.id.usernameText);
        final TextView karmaText = headerView.findViewById(R.id.karmaText);
        final TextView ageText = headerView.findViewById(R.id.ageText);
        final TextView ageTextMessage = headerView.findViewById(R.id.ageTextMessage);
        final ImageView userImage = headerView.findViewById(R.id.headerUserImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.homepageLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get value for drawer
        GetRequest pr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    usernameText.setText(jo.getString("name"));
                    karmaText.setText( String.valueOf(jo.getInt("link_karma") + jo.getInt("comment_karma")) );
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
                    ageText.setText(String.valueOf(age.intValue()));
                    ageTextMessage.setText(message);
                    new DownloadImageTask(userImage).execute(jo.getString("icon_img"));
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        String auth = sharedPreferences.getString("access_token","");
        pr.execute(RedditAPI.getCallBaseURL()+"api/v1/me", "Bearer", auth);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}

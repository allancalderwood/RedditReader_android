package redditreader.com.redditreader_android.screens;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.DownloadImageTask;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONObject;

public class HomepageActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;
    Button homepageButton;
    Button popularButton;
    EditText searchText;
    LinearLayout postViewLayout;
    boolean homeSection =true;
    boolean popularSection =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        drawerView = findViewById(R.id.drawerView);
        updateDrawer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.homepageLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        homepageButton = findViewById(R.id.buttonHomepage);
        popularButton = findViewById(R.id.buttonPopular);
        postViewLayout = findViewById(R.id.postViewLayout);
        searchText = findViewById(R.id.searchText);
        loadHomepage();
        searchText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO Search
                System.out.println("SEARCHED FOR: "+searchText.getText().toString());
            }
        });

        homepageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(popularSection){
                    popularSection=false;
                    homeSection=true;
                    popularButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    popularButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                    homepageButton.setTextColor(getResources().getColor(R.color.colorContrast));
                    homepageButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
                    loadHomepage();
                }
            }
        });

        popularButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(homeSection){
                    homeSection=false;
                    popularSection=true;
                    homepageButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    homepageButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                    popularButton.setTextColor(getResources().getColor(R.color.colorContrast));
                    popularButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
                    loadPopular();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void loadHomepage(){
        //TODO load homepage posts
    }

    private void loadPopular(){
        //TODO load homepage posts
    }

    private void updateDrawer(){
        View headerView = drawerView.getHeaderView(0);
        final TextView usernameText = headerView.findViewById(R.id.usernameText);
        final TextView karmaText = headerView.findViewById(R.id.karmaText);
        final TextView ageText = headerView.findViewById(R.id.ageText);
        final TextView ageTextMessage = headerView.findViewById(R.id.ageTextMessage);
        final ImageView userImage = headerView.findViewById(R.id.headerUserImage);

        if(!User.isUpdated()){ // i.e user is not setup yet
            // Get user details
            GetRequest gr = new GetRequest(new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    String[] response = (String[]) output;
                    try{
                        JSONObject jo = new JSONObject( response[1] );
                        System.out.println("RESPONSE: "+response[1]);
                        usernameText.setText(jo.getString("name"));
                        karmaText.setText(( String.valueOf(jo.getInt("link_karma") + jo.getInt("comment_karma")) ));
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
                        ageText.setText((age.intValue()));
                        ageTextMessage.setText(message);
                        new DownloadImageTask(userImage).execute(jo.getString("icon_img"));
                    }catch (Exception e){
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            });
            String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
            gr.execute(RedditAPI.getCallBaseURL()+"api/v1/me", "Bearer", auth);
        }else{
        usernameText.setText(User.getUsername());
        karmaText.setText(String.valueOf(User.getKarma()));
        ageText.setText(String.valueOf(User.getAccountAge()));
        ageTextMessage.setText(User.getAccountAgePostfix());
        new DownloadImageTask(userImage).execute(User.getProfileURL());
    }
    }
}

package redditreader.com.redditreader_android.screens;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.Trophy;
import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;
import redditreader.com.redditreader_android.widgets.TrophyListAdapter;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;
import static redditreader.com.redditreader_android.utils.TrophyFactory.trophyFact;

public class ProfileActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;
    private String username;
    private String profileURL;
    private String age;
    private int karma;
    private ImageView profileImage;
    private TextView profileUsername;
    private TextView profileAge;
    private TextView profileKarma;
    private LinearLayout trophyLayout;
    private ListView trophyList;
    private Button messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        profileURL = bundle.getString("profileURL");
        age = bundle.getString("age");
        karma = bundle.getInt("karma");

        profileImage = findViewById(R.id.profileImage);
        profileUsername = findViewById(R.id.profileUsername);
        profileAge = findViewById(R.id.profileAge);
        profileKarma = findViewById(R.id.profileKarma);
        trophyLayout = findViewById(R.id.profileTrophyLayout);
        trophyList = findViewById(R.id.trophyList);
        messageButton = findViewById(R.id.messageButton);

        if(User.getUsername().equals(username)){
            getTrophies();
            trophyLayout.setVisibility(View.VISIBLE);
        }else{
            messageButton.setVisibility(View.VISIBLE);
            messageButton.setText("Message "+username);
        }

        Picasso.with(getApplicationContext()).load(profileURL).into(profileImage);
        profileUsername.setText(username);
        profileAge.setText(age);
        profileKarma.setText(Integer.toString(karma));

        drawerView = findViewById(R.id.drawerView);
        DrawerView.updateDrawer(drawerView, getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.profileLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void getTrophies(){
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    ArrayList<Trophy> trophies = new ArrayList<Trophy>();
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            getTrophies();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading trophies.");
                            trophyList.addView(t);
                        }
                    }else {
                        trophyFact(jo, trophies);
                        TrophyListAdapter adapter = new TrophyListAdapter(ProfileActivity.this, R.layout.trophy_layout, trophies);
                        trophyList.setAdapter(adapter);
                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/api/v1/user/"+username+"/trophies", "Bearer", auth);
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

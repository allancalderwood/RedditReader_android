package redditreader.com.redditreader_android.screens;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;

public class HomepageActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;
    Button homepageButton;
    Button popularButton;
    EditText searchText;
    ListView postList;
    ProgressBar progressBar;
    boolean homeSection =true;
    boolean popularSection =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        drawerView = findViewById(R.id.drawerView);
        DrawerView.updateDrawer(drawerView, getApplicationContext());

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
        postList = findViewById(R.id.postList);
        progressBar = findViewById(R.id.loadingProgress);
        searchText = findViewById(R.id.searchText);
        loadHomepage();

        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    intent.putExtra("query", searchText.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
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

    private void loadHomepage(){
        progressBar.setVisibility(View.VISIBLE);
        postList.setVisibility(View.GONE);
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    ArrayList<Post> posts = new ArrayList<>();
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            loadHomepage();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                       postFact(jo, posts);
                       PostListAdapter adapter = new PostListAdapter(HomepageActivity.this, R.layout.post_layout, posts);
                       postList.setAdapter(adapter);
                       progressBar.setVisibility(View.GONE);
                       postList.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/best?limit=500", "Bearer", auth);
    }

    private void loadPopular(){
        progressBar.setVisibility(View.VISIBLE);
        postList.setVisibility(View.GONE);
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    ArrayList<Post> posts = new ArrayList<>();
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            loadHomepage();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                        postFact(jo, posts);
                        PostListAdapter adapter = new PostListAdapter(HomepageActivity.this, R.layout.post_layout, posts);
                        postList.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        postList.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/r/all?limit=500", "Bearer", auth);
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

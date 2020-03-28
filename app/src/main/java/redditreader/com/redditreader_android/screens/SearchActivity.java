package redditreader.com.redditreader_android.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.Subreddit;
import redditreader.com.redditreader_android.models.UserOther;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;
import redditreader.com.redditreader_android.widgets.SubListAdapter;
import redditreader.com.redditreader_android.widgets.UserListAdapter;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;
import static redditreader.com.redditreader_android.utils.SubFactory.subFact;
import static redditreader.com.redditreader_android.utils.UserFactory.userFact;

public class SearchActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;
    ListView postList;
    ProgressBar progressBar;
    private String query;
    private TextView srchText;
    private Button postBtn;
    private Button subBtn;
    private Button userBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        query = bundle.getString("query");

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

        srchText = findViewById(R.id.searchTxt);
        postBtn = findViewById(R.id.buttonPosts);
        subBtn = findViewById(R.id.buttonSubs);
        userBtn = findViewById(R.id.buttonUsers);
        postList = findViewById(R.id.postList);
        progressBar = findViewById(R.id.loadingProgress);
        srchText.setText("Results for '"+query+"'");
        loadSearchPosts();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBtn.setTextColor(getResources().getColor(R.color.colorContrast));
                subBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                userBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                loadSearchPosts();
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subBtn.setTextColor(getResources().getColor(R.color.colorContrast));
                postBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                userBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                loadSearchSubs();
            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBtn.setTextColor(getResources().getColor(R.color.colorContrast));
                subBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                postBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                loadSearchUsers();
            }
        });

    }

    private void loadSearchPosts(){
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
                            loadSearchPosts();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                       postFact(jo, posts);
                       PostListAdapter adapter = new PostListAdapter(SearchActivity.this, R.layout.post_layout, posts);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/search.json?limit=100&q="+query+"&type=link", "Bearer", auth);
    }

    private void loadSearchSubs(){
        progressBar.setVisibility(View.VISIBLE);
        postList.setVisibility(View.GONE);
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    ArrayList<Subreddit> subs = new ArrayList<>();
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            loadSearchSubs();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                        subFact(jo, subs);
                        SubListAdapter adapter = new SubListAdapter(SearchActivity.this, R.layout.sub_layout, subs);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/search.json?limit=100&q="+query+"&type=sr", "Bearer", auth);
    }

    private void loadSearchUsers(){
        progressBar.setVisibility(View.VISIBLE);
        postList.setVisibility(View.GONE);
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    ArrayList<UserOther> users = new ArrayList<>();
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            loadSearchUsers();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                        userFact(jo, users);
                        UserListAdapter adapter = new UserListAdapter(SearchActivity.this, R.layout.user_layout, users);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/search.json?limit=100&q="+query+"&type=user", "Bearer", auth);
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

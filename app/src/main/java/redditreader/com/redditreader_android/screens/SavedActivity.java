package redditreader.com.redditreader_android.screens;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;

public class SavedActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    NavigationView drawerView;
    ListView postList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
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

        postList = findViewById(R.id.postList);
        progressBar = findViewById(R.id.loadingProgress);
        loadSaved();
    }

    private void loadSaved(){
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
                            loadSaved();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                       postFact(jo, posts);
                       PostListAdapter adapter = new PostListAdapter(SavedActivity.this, R.layout.post_layout, posts);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/user/"+ User.getUsername()+"/saved.json", "Bearer", auth);
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

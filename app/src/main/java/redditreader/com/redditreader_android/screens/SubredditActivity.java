package redditreader.com.redditreader_android.screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.Subreddit;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;
import static redditreader.com.redditreader_android.utils.SubFactory.subFact;

public class SubredditActivity extends AppCompatActivity {
    private  String subreddit;
    private DrawerLayout drawer;
    NavigationView drawerView;
    private TextView subredditText;
    private ImageView subIcon;
    private Chip subscribeChip;
    private LinearLayout subHeader;
    private Spinner filter;
    private boolean subbed = false;
    EditText searchText;
    ListView postList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        subreddit = bundle.getString("subreddit");

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

        filter = findViewById(R.id.filter);
        subredditText = findViewById(R.id.subredditText);
        subIcon = findViewById(R.id.subIcon);
        subscribeChip = findViewById(R.id.subscribed);
        subHeader = findViewById(R.id.subHeader);
        postList = findViewById(R.id.postList);
        progressBar = findViewById(R.id.loadingProgress);
        searchText = findViewById(R.id.searchText);

        searchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Intent intent = new Intent(getApplicationContext(), SearchSubredditActivity.class);
                    intent.putExtra("query", searchText.getText().toString());
                    intent.putExtra("subreddit", subreddit);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        subredditText.setText("R/"+subreddit);
        String[] filters = new String[]{"Hot","Top","New"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        filter.setAdapter(adapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadFilter(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        subscribeChip.setBackgroundColor(Color.LTGRAY);
        subscribeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subbed){
                    subbed=false;
                    RedditAPI.subscribe(subreddit, "unsub");
                    subscribeChip.setText("Subscribe");
                    subscribeChip.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    subbed=true;
                    RedditAPI.subscribe(subreddit, "sub");
                    subscribeChip.setText("Unsubscribe");
                    subscribeChip.setBackgroundColor(Color.LTGRAY);
                }
            }
        });



        loadSub();
        loadSubPosts();
        checkSub();
        searchText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO Search
                System.out.println("SEARCHED FOR: "+searchText.getText().toString());
            }
        });
    }

    private void loadFilter(String filter){
        String filtered;
        if (filter.equals("Hot")){
            filtered="";
        }else{
            filtered="/"+filter.toLowerCase()+"/";
        }
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
                            loadSub();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading subreddit.");
                            postList.addView(t);
                        }
                    }else {
                        postFact(jo, posts);
                        PostListAdapter adapter = new PostListAdapter(SubredditActivity.this, R.layout.post_layout, posts);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/r/"+subreddit+""+filtered+".json?limit=200", "Bearer", auth);
    }

    private void loadSub(){
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
                            loadSub();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading subreddit.");
                            postList.addView(t);
                        }
                    }else {
                        JSONObject subData = jo.getJSONObject("data");
                        Picasso.with(getApplicationContext()).load(subData.getString("icon_img")).placeholder(R.drawable.ic_subreddit_icon)
                                .error(R.drawable.ic_subreddit_icon).into(subIcon);
                        Picasso.with(getApplicationContext()).load(subData.getString("banner_background_image")).centerCrop().into(new Target(){

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                subHeader.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(final Drawable errorDrawable) {
                                Log.d("TAG", "FAILED");
                            }

                            @Override
                            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                Log.d("TAG", "Prepare Load");
                            }
                        });

                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/r/"+subreddit+"/about.json?", "Bearer", auth);
    }

    private void loadSubPosts(){
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
                            loadSub();
                        }else{
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Error loading posts.");
                            postList.addView(t);
                            progressBar.setVisibility(View.GONE);
                            postList.setVisibility(View.VISIBLE);
                        }
                    }else {
                       postFact(jo, posts);
                       PostListAdapter adapter = new PostListAdapter(SubredditActivity.this, R.layout.post_layout, posts);
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
        gr.execute(RedditAPI.getCallBaseURL()+"/r/"+subreddit+".json?limit=200", "Bearer", auth);
    }

    private void checkSub(){
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
                            loadSub();
                        }
                    }else {
                        subFact(jo, subs);
                        for (Subreddit s : subs) {
                            if (s.getName().equals(subreddit)){
                                subbed=true;
                                subscribeChip.setText("Subscribed");
                                subscribeChip.setBackgroundColor(Color.LTGRAY);
                                break;
                            }
                        }
                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/subreddits/mine/subscriber.json?limit=1000", "Bearer", auth);
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

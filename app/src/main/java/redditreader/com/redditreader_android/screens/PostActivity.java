package redditreader.com.redditreader_android.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;
import redditreader.com.redditreader_android.widgets.DrawerView;
import redditreader.com.redditreader_android.widgets.PostListAdapter;
import redditreader.com.redditreader_android.widgets.PostListAdapter2;

import static redditreader.com.redditreader_android.utils.PostFactory.postFact;

public class PostActivity extends AppCompatActivity {
    ListView postList;
    ProgressBar progressBar;

    private String id;
    private String subreddit;
    private String title;
    private String selftext;
    private int score;
    private String authorName;
    private String authorID;
    private String imageURL;
    private int imageWidth;
    private int imageHeight;
    private String imageURLPreview;
    private String time;
    private int numComments;
    private String url;
    private int numAwards;
    private String mediaURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        subreddit = bundle.getString("subreddit");
        title = bundle.getString("title");
        selftext = bundle.getString("selftext");
        score = bundle.getInt("karma");
        authorName = bundle.getString("author");
        imageURL = bundle.getString("imageURL");
        imageWidth = bundle.getInt("imageWidth");
        imageHeight = bundle.getInt("imageHeight");
        imageURLPreview = bundle.getString("imageURLPReview");
        time = bundle.getString("time");
        numComments = bundle.getInt("numComments");
        url = bundle.getString("url");
        numAwards = bundle.getInt("numAwards");

        postList = findViewById(R.id.postList);
        progressBar = findViewById(R.id.loadingProgress);
        initPost();

    }

    private void initPost(){
        ArrayList<Post> posts = new ArrayList<>();
        Post post = new Post(id, "", authorName,
                imageURL, imageURLPreview, imageHeight, imageWidth,
        title, selftext, subreddit,
        score, numComments, time, url, numAwards);
        posts.add(post);

        PostListAdapter2 adapter = new PostListAdapter2(PostActivity.this, R.layout.post_layout2, posts);
        postList.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        postList.setVisibility(View.VISIBLE);
    }

}
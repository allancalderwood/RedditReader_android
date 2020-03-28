package redditreader.com.redditreader_android.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.screens.PostActivity;
import redditreader.com.redditreader_android.screens.ProfileActivity;
import redditreader.com.redditreader_android.screens.SubredditActivity;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;

public class PostListAdapter2 extends ArrayAdapter<Post> {
    private static final String TAG = "CustomPostAdapter";
    private Context context;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        private TextView title;
        private Chip subreddit;
        private Chip awards;
        private Chip time;
        private ImageView image;
        private TextView selftext;
        private TextView karma;
        private TextView comments;
        private Chip author;
        private ImageButton upvote;
        private ImageButton downvote;
        private ImageButton save;
        private ImageButton options;
        private LinearLayout secondaryLayout;
        private boolean upvoted = false;
        private boolean downvoted = false;
        private boolean saved = false;
    }

    public PostListAdapter2(Context context, int resource, ArrayList<Post> posts){
        super(context, resource, posts);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final String id = getItem(position).getId();
        final String title = getItem(position).getTitle();
        final String selftext = getItem(position).getSelftext();
        final String subreddit = getItem(position).getSubreddit();
        String time = getItem(position).getTime();
        final int numAwards = getItem(position).getNumAwards();
        final String imgUrl = getItem(position).getImageURL();
        final String previewUrl = getItem(position).getImageURLPreview();
        final int karma = getItem(position).getScore();
        final int numComments = getItem(position).getNumComments();
        final String author = getItem(position).getAuthorName();
        final String link = getItem(position).getUrl();
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.subreddit = convertView.findViewById(R.id.subreddit);
            holder.time = convertView.findViewById(R.id.time);
            holder.selftext = convertView.findViewById(R.id.selftext);
            holder.image = convertView.findViewById(R.id.image);
            holder.karma = convertView.findViewById(R.id.karma);
            holder.awards = convertView.findViewById(R.id.awards);
            holder.comments = convertView.findViewById(R.id.comments);
            holder.author = convertView.findViewById(R.id.author);
            holder.upvote = convertView.findViewById(R.id.upvote);
            holder.downvote = convertView.findViewById(R.id.downvote);
            holder.save = convertView.findViewById(R.id.save);
            holder.options = convertView.findViewById(R.id.options);
            holder.secondaryLayout = convertView.findViewById(R.id.secondaryDataLayout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        holder.title.setText(title);
        holder.subreddit.setText(subreddit);
        holder.subreddit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, SubredditActivity.class);
                intent.putExtra("subreddit", subreddit);
                context.startActivity(intent);
            }
        });

        if(numAwards>=1){
            holder.awards.setChipBackgroundColorResource(R.color.colorAwards);
            holder.awards.setText(Integer.toString(numAwards));
            holder.awards.setVisibility(View.VISIBLE);
        }
        holder.time.setText(time);
        if(!( previewUrl.equals("self") || previewUrl.equals("spoiler") )){
            Picasso.with(context).load(imgUrl).into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
            holder.selftext.setText(selftext);
        }else{
            holder.selftext.setText(selftext);
            holder.image.setVisibility(View.GONE);
        }
        holder.karma.setText(karma+" pts");
        holder.comments.setText(numComments+" comments");
        holder.author.setText(author);
        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUser(author);
            }
        });
        holder.upvote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(holder.upvoted){
                    holder.upvoted=false;
                    RedditAPI.vote(id,0);
                    holder.upvote.clearColorFilter();
                    holder.karma.setText((karma)+" pts");
                }else{
                    holder.upvoted=true;
                    RedditAPI.vote(id,1);
                    holder.upvote.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                    holder.downvote.clearColorFilter();
                    holder.karma.setText((karma+1)+" pts");
                }
            }
        });

        holder.downvote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(holder.downvoted){
                    holder.downvoted=false;
                    RedditAPI.vote(id,0);
                    holder.karma.setText((karma)+" pts");
                    holder.downvote.clearColorFilter();
                }else{
                    holder.downvoted=true;
                    RedditAPI.vote(id,-1);
                    holder.downvote.setColorFilter(Color.RED);
                    holder.upvote.clearColorFilter();
                    holder.karma.setText((karma-1)+" pts");
                }
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(holder.saved){
                    holder.saved=false;
                    RedditAPI.save(id,"Post");
                    holder.save.clearColorFilter();
                }else{
                    holder.saved=true;
                    RedditAPI.unsave(id);
                    holder.save.setColorFilter(Color.YELLOW);;
                }
            }
        });

        holder.options.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setMessage("What do you wish to do with this post?");
                alertBuilder.setPositiveButton("Share", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Reddit Post.");
                        intent.putExtra(Intent.EXTRA_TEXT, link);
                        context.startActivity(Intent.createChooser(intent, "Share"));
                    }
                });
                alertBuilder.setNeutralButton("Comment", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO COMMENT
                    }
                });
                alertBuilder.setNegativeButton("Report", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO REPORT
                    }
                });
                alertBuilder.show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("selftext", selftext);
                intent.putExtra("subreddit", subreddit);
                intent.putExtra("time", selftext);
                intent.putExtra("karma", karma);
                intent.putExtra("karma", karma);
                intent.putExtra("numAwards", numAwards);
                intent.putExtra("numComments", numComments);
                intent.putExtra("url", getItem(position).getUrl());
                intent.putExtra("imageURL", imgUrl);
                intent.putExtra("imageURLPReview", previewUrl);
                intent.putExtra("author", author);
                intent.putExtra("imageHeight", getItem(position).getImageHeight());
                intent.putExtra("imageWidth", getItem(position).getImageWidth());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void goToUser(final String username){
        GetRequest gr = new GetRequest(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String[] response = (String[]) output;
                try{
                    JSONObject jo = new JSONObject( response[1] );
                    if(jo.has("message")){
                        if(jo.getString("message").equals("Unauthorized")){
                            RedditAPI.refreshToken();
                            goToUser(username);
                        }
                    }else {
                        JSONObject userData = jo.getJSONObject("data");
                        long seconds = userData.getLong("created")/10;
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

                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("username", userData.getString("name"));
                        intent.putExtra("profileURL", userData.getString("icon_img"));
                        intent.putExtra("age", (age.intValue()+message));
                        intent.putExtra("karma", (userData.getInt("link_karma")+userData.getInt("comment_karma")));
                        context.startActivity(intent);
                    }
                }catch (Exception e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        String auth = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
        gr.execute(RedditAPI.getCallBaseURL()+"/user/"+username+"/about.json", "Bearer", auth);
    }


}

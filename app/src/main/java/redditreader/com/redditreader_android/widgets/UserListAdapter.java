package redditreader.com.redditreader_android.widgets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Subreddit;
import redditreader.com.redditreader_android.models.UserOther;
import redditreader.com.redditreader_android.screens.ProfileActivity;
import redditreader.com.redditreader_android.screens.SubredditActivity;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;

public class UserListAdapter extends ArrayAdapter<UserOther> {
    private static final String TAG = "CustomSubredditAdapter";
    private Context context;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        private TextView title;
        private ImageView image;
    }

    public UserListAdapter(Context context, int resource, ArrayList<UserOther> users){
        super(context, resource, users);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final String usrename = getItem(position).get_username();
        String imageUrl = getItem(position).get_profileURL();
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.usermame);
            holder.image = convertView.findViewById(R.id.userIcon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        holder.title.setText(usrename);
        if(imageUrl.isEmpty()){
            holder.image.setImageResource(R.drawable.ic_subreddit_icon);
        }else{
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_subreddit_icon)
                    .error(R.drawable.ic_subreddit_icon).into(holder.image);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUser(usrename);
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

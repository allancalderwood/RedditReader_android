package redditreader.com.redditreader_android.widgets;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import redditreader.com.redditreader_android.MainActivity;
import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.User;
import redditreader.com.redditreader_android.screens.ProfileActivity;
import redditreader.com.redditreader_android.screens.SavedActivity;
import redditreader.com.redditreader_android.utils.AsyncResponse;
import redditreader.com.redditreader_android.utils.GetRequest;
import redditreader.com.redditreader_android.utils.RedditAPI;

public class DrawerView {
    static public void updateDrawer(NavigationView drawerView, Context c){
        final Context context = c;
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
                        Picasso.with(context).load(jo.getString("icon_img")).into(userImage);
                    }catch (Exception e){
                        System.err.println(e.getLocalizedMessage());
                    }
                }
            });
            String auth = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE).getString("access_token","");
            gr.execute(RedditAPI.getCallBaseURL()+"api/v1/me", "Bearer", auth);
        }else{
            usernameText.setText(User.getUsername());
            karmaText.setText(String.valueOf(User.getKarma()));
            ageText.setText(String.valueOf(User.getAccountAge()));
            ageTextMessage.setText(User.getAccountAgePostfix());
            Picasso.with(context).load(User.getProfileURL()).into(userImage);
        }
        drawerView.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("username", User.getUsername());
                intent.putExtra("profileURL", User.getProfileURL());
                intent.putExtra("age", User.getAccountAge()+User.getAccountAgePostfix());
                intent.putExtra("karma", User.getKarma());
                context.startActivity(intent);
                return true;
            }
        });
        drawerView.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println("VIEW SUBS!!!!!!!!!!!!!!!!!");
                return true;
            }
        });
        drawerView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, SavedActivity.class);
                context.startActivity(intent);
                return true;
            }
        });
    }
}

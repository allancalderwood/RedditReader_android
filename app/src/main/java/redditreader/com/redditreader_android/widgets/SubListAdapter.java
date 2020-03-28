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

import java.util.ArrayList;

import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Subreddit;
import redditreader.com.redditreader_android.models.Trophy;
import redditreader.com.redditreader_android.screens.SubredditActivity;

public class SubListAdapter extends ArrayAdapter<Subreddit> {
    private static final String TAG = "CustomSubredditAdapter";
    private Context context;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        private TextView title;
        private ImageView image;
    }

    public SubListAdapter(Context context, int resource, ArrayList<Subreddit> subs){
        super(context, resource, subs);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final String title = getItem(position).getName();
        String imageUrl = getItem(position).getIconURL();
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.subName);
            holder.image = convertView.findViewById(R.id.subIcon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        holder.title.setText(title);
        if(imageUrl.isEmpty()){
            holder.image.setImageResource(R.drawable.ic_subreddit_icon);
        }else{
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_subreddit_icon)
                    .error(R.drawable.ic_subreddit_icon).into(holder.image);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubredditActivity.class);
                intent.putExtra("subreddit", title);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


}

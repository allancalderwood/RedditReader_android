package redditreader.com.redditreader_android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.utils.DownloadImageTask;

public class PostListAdapter extends ArrayAdapter<Post> {
    private static final String TAG = "CustomPostAdapter";
    private Context context;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        private TextView title;
        private Chip subreddit;
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

    }

    public PostListAdapter(Context context, int resource, ArrayList<Post> posts){
        super(context, resource, posts);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String title = getItem(position).getTitle();
        String selftext = getItem(position).getSelftext();
        String subreddit = getItem(position).getSubreddit();
        String time = getItem(position).getTime();
        String imgUrl = getItem(position).getImageURL();
        String previewUrl = getItem(position).getImageURLPreview();
        int karma = getItem(position).getScore();
        int numComments = getItem(position).getNumComments();
        String author = getItem(position).getAuthorName();

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
        holder.time.setText(time);
        if(!( previewUrl.equals("self") || previewUrl.equals("spoiler") )){
            new DownloadImageTask(holder.image).execute(imgUrl);
            holder.image.setVisibility(View.VISIBLE);

        }else{
            holder.selftext.setText(selftext);
            holder.image.setVisibility(View.GONE);
        }
        holder.karma.setText(karma+" pts");
        holder.comments.setText(numComments+" comments");
        holder.author.setText(author);

        return convertView;
    }

}

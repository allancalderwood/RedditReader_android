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

import java.util.ArrayList;

import redditreader.com.redditreader_android.R;
import redditreader.com.redditreader_android.models.Post;
import redditreader.com.redditreader_android.models.Trophy;
import redditreader.com.redditreader_android.utils.RedditAPI;

public class TrophyListAdapter extends ArrayAdapter<Trophy> {
    private static final String TAG = "CustomTrophyAdapter";
    private Context context;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder{
        private TextView title;
        private ImageView image;
    }

    public TrophyListAdapter(Context context, int resource, ArrayList<Trophy> trophies){
        super(context, resource, trophies);
        this.context = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String title = getItem(position).getName();
        String imageUrl = getItem(position).getIconURL();
        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.image = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;
        holder.title.setText(title);
        Picasso.with(context).load(imageUrl).into(holder.image);

        return convertView;
    }


}

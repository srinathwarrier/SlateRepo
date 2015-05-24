package com.slate1.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.slate1.R;
import com.slate1.activities.SlateActivity;
import com.slate1.entities.Song;
import com.slate1.entities.YoutubeSong;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by I076630 on 06-May-15.
 */
public class YoutubeSongListAdapter extends BaseAdapter {
    ArrayList<YoutubeSong> mYoutubeSongArrayList;
    private LayoutInflater inflater;
    private Context mContext;

    public YoutubeSongListAdapter(ArrayList<YoutubeSong> youtubeSongArrayList, Activity context){
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mYoutubeSongArrayList= youtubeSongArrayList;
        this.mContext = context;

        File cacheDir = StorageUtils.getCacheDirectory(context);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.drawable.ic_dialog_alert) // resource or drawable
                .showImageForEmptyUri(android.R.drawable.ic_dialog_alert) // resource or drawable
                .showImageOnFail(android.R.drawable.ic_dialog_alert) // resource or drawable
                .resetViewBeforeLoading(true)  // default
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);

    }


    @Override
    public int getCount() {
        return mYoutubeSongArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mHolder;
        if (convertView == null) {
            mHolder = new MyViewHolder();
            convertView = inflater.inflate(R.layout.song_list_item, null);

            mHolder.thumbnailImageView= (ImageView) convertView.findViewById(R.id.thumbnailImageView);
            mHolder.songNameTextView = (TextView) convertView.findViewById(R.id.songtextView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        if(mYoutubeSongArrayList.size()==0){
            return convertView;
        }

        mHolder.songNameTextView.setText(mYoutubeSongArrayList.get(position).getSongDescription());
        ImageLoader.getInstance().displayImage(mYoutubeSongArrayList.get(position).getThumbnailLink(), mHolder.thumbnailImageView);

        return convertView;
    }



    static class MyViewHolder {
        ImageView thumbnailImageView;
        TextView songNameTextView;
    }


}

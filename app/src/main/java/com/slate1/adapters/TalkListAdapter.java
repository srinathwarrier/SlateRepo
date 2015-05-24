package com.slate1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.slate1.R;
import com.slate1.entities.Talk;
import com.slate1.entities.YoutubeSong;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by I076630 on 06-May-15.
 */
public class TalkListAdapter extends BaseAdapter {
    ArrayList<Talk> mTalkArrayList;
    private LayoutInflater inflater;
    private Context mContext;

    public TalkListAdapter(ArrayList<Talk> talkArrayList, Activity context){
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTalkArrayList= talkArrayList;
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
        return mTalkArrayList.size();
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
            convertView = inflater.inflate(R.layout.list_item_talk, null);

            mHolder.personNameTextView= (TextView) convertView.findViewById(R.id.talk_personName_TextView);
            mHolder.dateAddedTextView= (TextView) convertView.findViewById(R.id.talk_date_TextView);
            mHolder.talkTextView= (TextView) convertView.findViewById(R.id.talk_textView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        if(mTalkArrayList.size()==0){
            return convertView;
        }



        mHolder.personNameTextView.setText(mTalkArrayList.get(position).getName());
        mHolder.dateAddedTextView.setText(mTalkArrayList.get(position).getDateAddedFormattedString());
        mHolder.talkTextView.setText(mTalkArrayList.get(position).getTalkText());

        return convertView;
    }



    static class MyViewHolder {
        //ImageView personImageView;
        TextView personNameTextView;
        TextView dateAddedTextView;
        TextView talkTextView;
    }


}

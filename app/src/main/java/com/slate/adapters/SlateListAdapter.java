package com.slate.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slate.R;
import com.slate.entities.Song;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by I076630 on 06-May-15.
 */
public class SlateListAdapter extends BaseAdapter {
    ArrayList<Song> mSongArrayList;
    private LayoutInflater inflater;

    public SlateListAdapter(ArrayList<Song> songArrayList , Activity context){
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSongArrayList= songArrayList;
    }


    @Override
    public int getCount() {
        return mSongArrayList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mHolder;
        if (convertView == null) {
            mHolder = new MyViewHolder();
            convertView = inflater.inflate(R.layout.slate_list_item, null);


            mHolder.containerFrameLayout = (FrameLayout) convertView.findViewById(R.id.container);
            //mHolder.songIdTextView= (TextView) convertView.findViewById(R.id.outletname);
            mHolder.friendNameTextView= (TextView) convertView.findViewById(R.id.friendNameTextView);
            mHolder.dateAddedTextView= (TextView) convertView.findViewById(R.id.dateAddedTextView);
            mHolder.songDescTextView= (TextView) convertView.findViewById(R.id.songDescTextView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }
        String formattedString="";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(mSongArrayList.get(position).getDateAdded());
            sdf = new SimpleDateFormat("EEE, MMM d, yyyy, h:mm a");
            formattedString = sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(mSongArrayList.size()==0){
            return convertView;
        }

        //mHolder.songIdTextView.setText(mSongArrayList.get(position).getSongID());
        mHolder.friendNameTextView.setText(mSongArrayList.get(position).getFriendName());
        mHolder.dateAddedTextView.setText(formattedString);
        mHolder.songDescTextView.setText(mSongArrayList.get(position).getSongDescription());

        // If unread item, change color of dateAddedTextView to green
        if(mSongArrayList.get(position).getIsUnreadStatus().equals("1")){
            // background : #6EC5B8
            // date-text-color : material_blue_grey_800
            mHolder.containerFrameLayout.setBackgroundColor(Color.parseColor("#6EC5B8"));
            mHolder.dateAddedTextView.setTextColor(Color.parseColor("#ff37474f"));
        }
        else {
            // background :
            // date-text-color : #A6B3B4
            mHolder.containerFrameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            mHolder.dateAddedTextView.setTextColor(Color.parseColor("#A6B3B4"));
        }

        return convertView;
    }


    static class MyViewHolder {

        FrameLayout containerFrameLayout;

        //TextView songIdTextView;
        TextView friendNameTextView;
        TextView dateAddedTextView;
        TextView songDescTextView;

    }


}

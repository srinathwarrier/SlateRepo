package com.slate.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slate.R;
import com.slate.entities.Song;

import java.io.File;
import java.util.ArrayList;

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

            //mHolder.songIdTextView= (TextView) convertView.findViewById(R.id.outletname);
            //mHolder.frequencyTextView= (TextView) convertView.findViewById(R.id.textView);
            mHolder.dateAddedTextView= (TextView) convertView.findViewById(R.id.textView3);
            mHolder.songDescTextView= (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        //mHolder.songIdTextView.setText(mSongArrayList.get(position).getSongID());
        //mHolder.frequencyTextView.setText(mSongArrayList.get(position).getFrequency());
        mHolder.dateAddedTextView.setText(mSongArrayList.get(position).getDateAdded());
        mHolder.songDescTextView.setText(mSongArrayList.get(position).getSongDescription());

        return convertView;
    }


    static class MyViewHolder {

        //TextView songIdTextView;
        //TextView frequencyTextView;
        TextView dateAddedTextView;
        TextView songDescTextView;

    }


}

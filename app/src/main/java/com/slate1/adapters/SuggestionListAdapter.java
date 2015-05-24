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
import android.widget.TextView;

import com.slate1.R;
import com.slate1.activities.SlateActivity;
import com.slate1.entities.Suggestion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by I076630 on 06-May-15.
 */
public class SuggestionListAdapter extends BaseAdapter {
    ArrayList<Suggestion> mSuggestionArrayList;
    private LayoutInflater inflater;
    private Context mContext;

    public SuggestionListAdapter(ArrayList<Suggestion> suggestionArrayList, Activity context){
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSuggestionArrayList= suggestionArrayList;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mSuggestionArrayList.size();
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
            convertView = inflater.inflate(R.layout.slate_list_item, null);


            mHolder.suggestionTextView= (TextView) convertView.findViewById(R.id.friendNameTextView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        if(mSuggestionArrayList.size()==0){
            return convertView;
        }

        mHolder.suggestionTextView.setText(mSuggestionArrayList.get(position).getSuggestionName());
        return convertView;
    }



    static class MyViewHolder {

        TextView suggestionTextView;

    }


}

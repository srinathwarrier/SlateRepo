package com.slate1.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.slate1.R;
import com.slate1.activities.SlateActivity;
import com.slate1.asynctask.GetTalksAsyncTask;
import com.slate1.asynctask.SlateListAsyncTask;
import com.slate1.entities.Song;
import com.slate1.entities.Talk;
import com.slate1.video_test.VideoFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by I076630 on 06-May-15.
 */
public class SlateListAdapter extends BaseAdapter implements Filterable {
    ArrayList<Song> mSongArrayList;
    ArrayList<Song> mFilteredSongArrayList;
    private LayoutInflater inflater;
    private Context mContext;
    //private boolean isVideoPlaying;
    //private int currentVideoPosition;
    public int prevButtonPosition=-1;
    private ItemFilter mFilter = new ItemFilter();


    public SlateListAdapter(ArrayList<Song> songArrayList , Activity context){
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSongArrayList= songArrayList;
        resetFilteredSongArrayList();
        //mFilteredSongArrayList = songArrayList;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mFilteredSongArrayList.size();
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


            mHolder.containerFrameLayout = (FrameLayout) convertView.findViewById(R.id.container);
            //mHolder.songIdTextView= (TextView) convertView.findViewById(R.id.outletname);
            mHolder.friendNameTextView= (TextView) convertView.findViewById(R.id.friendNameTextView);
            mHolder.dateAddedTextView= (TextView) convertView.findViewById(R.id.dateAddedTextView);
            mHolder.songDescTextView= (TextView) convertView.findViewById(R.id.songDescTextView);
            mHolder.youtubeButton = (ImageButton) convertView.findViewById(R.id.youtubeButton);
            mHolder.talkButton = (ImageButton) convertView.findViewById(R.id.talkButton);
            mHolder.talkButtonBadgeTextView = (TextView)convertView.findViewById(R.id.talkButtonBadgeTextView);


            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }
        String formattedString=mFilteredSongArrayList.get(position).getDateAddedFormattedString();

        if(mFilteredSongArrayList.size()==0){
            return convertView;
        }

        mHolder.friendNameTextView.setText(mFilteredSongArrayList.get(position).getFriendName());
        mHolder.dateAddedTextView.setText(formattedString);
        mHolder.songDescTextView.setText(mFilteredSongArrayList.get(position).getSongDescription());

        // If unread item, change color of dateAddedTextView to green
        if(mFilteredSongArrayList.get(position).getIsUnreadStatus().equals("1")){
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

        mHolder.youtubeButton.setTag(position);
        SlateListAdapter.setImageButtonAsPlay(mHolder.youtubeButton);
        if(prevButtonPosition!=-1){
            if(prevButtonPosition == position){
                SlateListAdapter.setImageButtonAsStop(mHolder.youtubeButton);
            }
        }

        mHolder.youtubeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    ImageButton currentButton = (ImageButton) view;
                    int currentButtonPosition = (Integer) (view.getTag());
                    callCollapseSearchButton();
                    String youtubeUrl = mFilteredSongArrayList.get(currentButtonPosition).getYoutubeLink();

                    if (youtubeUrl != null && (!youtubeUrl.equals(""))) {
                        // Case 1 - 1st Play : prevButton is null. So player is inactive
                        if (prevButtonPosition == -1) {
                            callCollapseSearchButton();
                            SlateListAdapter.setImageButtonAsStop(currentButton);
                            callOpenYoutubeVideo(youtubeUrl);
                            prevButtonPosition = currentButtonPosition;
                        } else {
                            // Case 2 - Play another song : prevButton's position is different from currentButton's position
                            if (prevButtonPosition != currentButtonPosition) {
                                //Get prevButton from prevButtonPosition and set its image to play
                                //ImageButton prevButton = currentButton.getParent();
                                //SlateListAdapter.setImageButtonAsPlay(prevButton);

                                SlateListAdapter.setImageButtonAsStop(currentButton);
                                callOpenYoutubeVideo(youtubeUrl);
                                prevButtonPosition = currentButtonPosition;
                                notifyDataSetChanged();
                            }
                            // Case 3 - Stop the playing song : prevButton's position is the same as currentButton's position
                            else {
                                SlateListAdapter.setImageButtonAsPlay(currentButton);
                                callCloseYoutubeVideo();
                                prevButtonPosition = -1;
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mHolder.talkButton.setTag(position);
        mHolder.talkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int currentButtonPosition = (Integer) (view.getTag());
                String userSongId = mFilteredSongArrayList.get(currentButtonPosition).getUserSongID();


                callOpenTalkLinearLayout(userSongId);
                callCollapseSearchButton();
                callHideSearchButton();

                callRevertPlayButtonAndCloseYoutubeVideo();
                if (mContext instanceof SlateActivity) {
                    ((SlateActivity) mContext).callGetTalkAsyncTask(userSongId);
                }
            }
        });

        mHolder.talkButtonBadgeTextView.setText(mFilteredSongArrayList.get(position).getNoOfTalks());
        if(mFilteredSongArrayList.get(position).getNoOfTalks().equals("0")){
            mHolder.talkButtonBadgeTextView.setVisibility(View.INVISIBLE);
        }
        else{
            mHolder.talkButtonBadgeTextView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    static class MyViewHolder {

        FrameLayout containerFrameLayout;

        //TextView songIdTextView;
        TextView friendNameTextView;
        TextView dateAddedTextView;
        TextView songDescTextView;
        ImageButton youtubeButton;
        ImageButton talkButton;
        TextView talkButtonBadgeTextView;

        ImageButton prevYoutubeButton;

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<Song> list = mSongArrayList;

            int count = list.size();
            final ArrayList<Song> nlist = new ArrayList<Song>(count);

            Song filterableSong ;

            for (int i = 0; i < count; i++) {
                filterableSong = list.get(i);
                if (filterableSong.getSongDescription().toLowerCase().contains(filterString) || filterableSong.getFriendName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableSong);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredSongArrayList = (ArrayList<Song>) results.values;
            notifyDataSetChanged();
        }

    }

    public void resetFilteredSongArrayList(){
        int count = mSongArrayList.size();
        mFilteredSongArrayList = new ArrayList();

        Song filterableSong ;

        for (int i = 0; i < count; i++) {
            filterableSong = mSongArrayList.get(i);
            mFilteredSongArrayList.add(filterableSong);
        }
        notifyDataSetChanged();
    }

    public static void setImageButtonAsPlay(ImageButton imageButton){
        imageButton.setImageResource(android.R.drawable.ic_media_play);
    }

    public static void setImageButtonAsStop(ImageButton imageButton){
        imageButton.setImageResource(R.drawable.ic_media_stop);
    }

    public void callOpenYoutubeVideo(String youtubeUrl){
        String youtubeCode = youtubeUrl.replace("https://www.youtube.com/watch?v=", "");
        if(mContext instanceof SlateActivity){
            ((SlateActivity)mContext).openYoutubeVideo(youtubeCode);
        }
    }
    public void callCloseYoutubeVideo(){
        if(mContext instanceof SlateActivity){
            ((SlateActivity)mContext).closeYoutubeVideo();
        }
    }

    public void callRevertPlayButtonAndCloseYoutubeVideo(){
        if(mContext instanceof SlateActivity){
            ((SlateActivity)mContext).revertPlayButtonAndCloseYoutubeVideo();
        }
    }

    public void callOpenTalkLinearLayout(String userSongId){
        if(mContext instanceof SlateActivity){
            ((SlateActivity)mContext).openTalkLinearLayout(userSongId);
        }
    }
    public void callCloseTalkLinearLayout(){
        if(mContext instanceof SlateActivity){
            ((SlateActivity)mContext).closeTalkLinearLayout();
        }
    }

    public void callCollapseSearchButton(){
        if(mContext instanceof SlateActivity) {
            ((SlateActivity) mContext).collapseSearchButton();
        }
    }

    public void callHideSearchButton(){
        if(mContext instanceof SlateActivity) {
            ((SlateActivity) mContext).hideSearchButton();
        }
    }







}

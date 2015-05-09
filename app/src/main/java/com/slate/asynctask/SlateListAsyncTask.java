package com.slate.asynctask;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;

import com.slate.adapters.SlateListAdapter;
import com.slate.entities.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by I076630 on 06-May-15.
 */
public class SlateListAsyncTask extends AsyncTask<Void,Void,Void>{

    Song songObject;

    ArrayList<Song> mSongArrayList;
    SlateListAdapter mSlateListAdapter;
    String userId;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public SlateListAsyncTask(ArrayList<Song> songArrayList,SlateListAdapter adapter,String id , SwipeRefreshLayout mSwipeRefreshLayout)
    {
        this.userId=id;
        this.mSlateListAdapter =adapter;
        this.mSongArrayList=songArrayList;
        this.mSwipeRefreshLayout =mSwipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mSongArrayList.clear();
        StringBuilder builder = null;
        try {
            URL url = new URL("https://slate-muzak.rhcloud.com/getSlateItems.php?id="+userId);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            JSONArray json = new JSONArray(builder.toString());


            for (int i = 0; i < json.length(); i++) {
                Log.i("Brandstore - Outletlist", "Start ");
                songObject= new Song();
                JSONObject object = json.getJSONObject(i);

                songObject.setSongID(object.get("SongID").toString());
                songObject.setFrequency(object.get("Frequency").toString());
                songObject.setFriendName(object.get("Name").toString());
                songObject.setDateAdded(object.get("DateAdded").toString());
                songObject.setSongDescription(object.get("Description").toString());

                Log.i("Slate - song list","object:"+songObject.toString());
                mSongArrayList.add(songObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mSlateListAdapter.notifyDataSetChanged();
        if(this.mSwipeRefreshLayout !=null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}

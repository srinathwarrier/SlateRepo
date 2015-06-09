package com.slate1.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.slate1.adapters.SlateListAdapter;
import com.slate1.entities.Song;
import com.slate1.interfaces.SlateListAsyncResponse;
import com.slate1.util.Connections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by I076630 on 06-May-15.
 */
public class AddSongAsyncTask extends AsyncTask<Void,Void,String>{

    Song songObject;
    ArrayList<Song> mSongArrayList;

    String query;
    String userId;
    String youtubeLinkString;

    SlateListAdapter mSlateListAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    //public  SlateListAsyncResponse slateListAsyncResponseDelegate=null;

    public AddSongAsyncTask(String query, String userId , String youtubeLinkString, ArrayList<Song> songArrayList , SwipeRefreshLayout mSwipeRefreshLayout, SlateListAdapter adapter)
    {
        this.query=query;
        this.userId = userId;
        this.youtubeLinkString = youtubeLinkString;
        this.mSongArrayList=songArrayList;
        this.mSlateListAdapter = adapter;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    @Override
    protected String doInBackground(Void... params) {
        mSongArrayList.clear();
        StringBuilder builder = null;
        try {
            String urlString = new Connections().getAddSongURL(userId,query,youtubeLinkString);
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
            return builder.toString();
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);

        try {

            JSONArray json = new JSONArray(resultString);

            for (int i = 0; i < json.length(); i++) {
                Log.i("Brandstore - Outletlist", "Start ");
                songObject = new Song();
                JSONObject object = json.getJSONObject(i);

                songObject.setSongID(object.get("SongID").toString());
                songObject.setFrequency(object.get("Frequency").toString());
                songObject.setFriendName(object.get("Name").toString());
                songObject.setDateAdded(object.get("DateAdded").toString());
                songObject.setSongDescription(object.get("Description").toString());
                songObject.setIsUnreadStatus(object.get("Status").toString());
                songObject.setYoutubeLink(object.get("YoutubeLink").toString());
                songObject.setUserSongID(object.get("ID").toString());
                songObject.setNoOfTalks(object.get("No_of_Comments").toString());
                Log.i("Slate - song list", "object:" + songObject.toString());
                mSongArrayList.add(songObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        mSlateListAdapter.resetFilteredSongArrayList(); // includes notifyDataSetChanged()
        if(this.mSwipeRefreshLayout !=null){
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }
}

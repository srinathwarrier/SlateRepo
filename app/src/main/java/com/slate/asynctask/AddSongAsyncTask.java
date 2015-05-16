package com.slate.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.slate.adapters.SlateListAdapter;
import com.slate.entities.Song;
import com.slate.interfaces.SlateListAsyncResponse;

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
public class AddSongAsyncTask extends AsyncTask<Void,Void,Void>{

    Song songObject;
    ArrayList<Song> mSongArrayList;

    String query;
    String userId;

    SlateListAdapter mSlateListAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    //public  SlateListAsyncResponse slateListAsyncResponseDelegate=null;

    public AddSongAsyncTask(String query, String userId , ArrayList<Song> songArrayList , SwipeRefreshLayout mSwipeRefreshLayout, SlateListAdapter adapter)
    {
        this.query=query;
        this.userId = userId;
        this.mSongArrayList=songArrayList;
        this.mSlateListAdapter = adapter;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mSongArrayList.clear();
        StringBuilder builder = null;
        try {
            String query2 = Uri.encode(query, "UTF-8");
            String urlStr= "https://slate-muzak.rhcloud.com/addSong.php?id="+userId+"&song_name="+query;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            //URL url = new URL("https://slate-muzak.rhcloud.com/addSong.php?id=1&song_name="+query2);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            JSONArray json = new JSONArray(builder.toString());

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

                Log.i("Slate - song list", "object:" + songObject.toString());
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

        //Refresh the Slate
        //slateListAsyncResponseDelegate.refreshSlate();

    }
}

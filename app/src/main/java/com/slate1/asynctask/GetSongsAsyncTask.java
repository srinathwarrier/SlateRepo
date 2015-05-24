package com.slate1.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.slate1.adapters.YoutubeSongListAdapter;
import com.slate1.entities.YoutubeSong;
import com.slate1.interfaces.SuggestionAsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by I076630 on 06-May-15.
 */
public class GetSongsAsyncTask extends AsyncTask<Void,Void,String>{

    String suggestionString;
    Context mContext;
    String resultUserId;
    public SuggestionAsyncResponse delegate;
    ArrayList<YoutubeSong> mYoutubeSongArrayList;
    YoutubeSongListAdapter mAdapter;

    public GetSongsAsyncTask(String suggestionString,YoutubeSongListAdapter adapter, ArrayList<YoutubeSong> youtubeSongArrayList, Context mContext)
    {
        this.suggestionString = suggestionString;
        this.mContext = mContext;
        this.mYoutubeSongArrayList = youtubeSongArrayList;
        this.mAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("AsyncTask", "onPreExecute");
        if(delegate!=null){
            delegate.emptySuggestionListView();
        }
        mYoutubeSongArrayList.clear();
    }

    @Override
    protected String doInBackground(Void... params) {

        StringBuilder builder = null;
        try {
            URI uri = new URI(
                    "https",
                    "slate-muzak.rhcloud.com",
                    "/suggestion.php",
                    "suggestion="+this.suggestionString,
                    null);
            String request = uri.toASCIIString();
            URL url = new URL(request);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            String line;
            builder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(
                    urlConnection.getInputStream()
            );
            BufferedReader reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) builder.append(line);

            return (builder.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);

        try {
            JSONArray jsonArray = new JSONArray(resultString);

            for(int i=0;i< jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                YoutubeSong youtubeSongObject = new YoutubeSong();
                youtubeSongObject.setSongID(jsonObject.get("ID").toString());
                youtubeSongObject.setSongDescription(jsonObject.get("Description").toString());
                youtubeSongObject.setYoutubeLink(jsonObject.get("YoutubeLink").toString());
                youtubeSongObject.setThumbnailLink(jsonObject.get("Thumbnail").toString());
                mYoutubeSongArrayList.add(youtubeSongObject);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        mAdapter.notifyDataSetChanged();
    }
}

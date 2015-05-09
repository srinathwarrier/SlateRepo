package com.slate.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
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

    String query;
    String userId;

    public  SlateListAsyncResponse slateListAsyncResponseDelegate=null;

    public AddSongAsyncTask(String query, String userId)
    {
        this.query=query;
        this.userId = userId;
    }

    @Override
    protected Void doInBackground(Void... params) {
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

        //Refresh the Slate
        slateListAsyncResponseDelegate.refreshSlate();

    }
}

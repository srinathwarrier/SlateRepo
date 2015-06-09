package com.slate1.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.EditText;

import com.slate1.activities.SlateActivity;
import com.slate1.adapters.SlateListAdapter;
import com.slate1.adapters.TalkListAdapter;
import com.slate1.entities.Song;
import com.slate1.entities.Talk;
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
import java.util.ArrayList;

/**
 * Created by I076630 on 06-May-15.
 */
public class AddTalkAsyncTask extends AsyncTask<Void,Void,String>{

    String userId;
    String userSongId;
    String talkText;
    Context mContext;
    ProgressDialog progress;

    ArrayList<Talk> mTalkArrayList;
    TalkListAdapter mTalkListAdapter;

    EditText mEditText;

    public AddTalkAsyncTask(String userId, String userSongId, String talkText, ArrayList<Talk> mTalkArrayList, TalkListAdapter mTalkListAdapter,Context mContext,EditText mEditText)
    {
        this.userId = userId;
        this.userSongId= userSongId;
        this.talkText = talkText;
        this.mTalkArrayList = mTalkArrayList;
        this.mTalkListAdapter = mTalkListAdapter;
        this.mContext = mContext;
        this.mEditText= mEditText;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mTalkArrayList.clear();

        progress = new ProgressDialog(this.mContext);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlString = new Connections().getAddTalkURL(userId,userSongId,talkText);
            URL url = new URL(urlString);
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
        }catch(Exception e){
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
                Talk talkObject = new Talk();
                talkObject.setTalkText(jsonObject.get("CommentText").toString());
                talkObject.setName(jsonObject.get("Name").toString());
                talkObject.setDateAdded(jsonObject.get("TimeAdded").toString());
                mTalkArrayList.add(talkObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        mTalkListAdapter.notifyDataSetChanged();
        mEditText.setText("");
        progress.dismiss();

    }
}

package com.slate1.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.slate1.activities.SlateActivity;
import com.slate1.util.Connections;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by I076630 on 06-May-15.
 */
public class AddUserAsyncTask extends AsyncTask<Void,Void,String>{

    String userName;
    String userEmail;
    String android_id;
    String registrationId;
    Context mContext;
    String resultUserId;
    ProgressDialog progress;

    public AddUserAsyncTask(String userName, String userEmail, String android_id, String registrationId, Context mContext)
    {
        this.userName = userName;
        this.userEmail = userEmail;
        this.android_id = android_id;
        this.registrationId = registrationId;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

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
            String urlString = new Connections().getAddUserURL(android_id,userName,userEmail,registrationId);
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
        }catch (MalformedURLException e) {
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
            JSONArray json = new JSONArray();
            Log.i("Slate:AddUser","1:"+json.toString() +" 2:"+json.get(0) );

            // Get userId and fill into this.resultUserId
            if(json.get(0) != "null")
            {
                this.resultUserId = ""+json.get(0);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        if(resultUserId!=null)
        {
            //Add new userId to SharedPreferences
            SharedPreferences settings = mContext.getSharedPreferences("MyPrefsFile", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("userId", resultUserId);
            editor.commit();

            progress.dismiss();

            // After new user is assigned, Open SlateActivity
            Intent intent = new Intent(mContext.getApplicationContext(), SlateActivity.class);
            mContext.startActivity(intent);
        }


    }
}

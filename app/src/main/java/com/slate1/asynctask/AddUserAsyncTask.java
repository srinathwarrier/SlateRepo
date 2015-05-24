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
public class AddUserAsyncTask extends AsyncTask<Void,Void,Void>{

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
    protected Void doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlStr= "https://slate-muzak.rhcloud.com/createNewUser.php?android_id='"+android_id+"'&name='"+userName+"'&email='"+userEmail+"'&reg_id='"+registrationId+"'";
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
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

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

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

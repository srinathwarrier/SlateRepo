package com.slate1.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.slate1.interfaces.CheckUserAsyncResponse;
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

/**
 * Created by I076630 on 06-May-15.
 */
public class UpdateRegistrationIdAsyncTask extends AsyncTask<Void,Void,Void>{


    String userId;
    String registrationId;
    Context mContext;

    public UpdateRegistrationIdAsyncTask(String userId, String registrationId , Context mContext)
    {
        this.userId=userId;
        this.registrationId=registrationId;
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlString = new Connections().getUpdateRegIdURL(userId,registrationId);
            URL url = new URL(urlString);
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
            JSONArray jsonArray = new JSONArray(builder.toString());
            Log.i("Slate:CheckUser", "JSON :" + jsonArray.toString());


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


    }
}

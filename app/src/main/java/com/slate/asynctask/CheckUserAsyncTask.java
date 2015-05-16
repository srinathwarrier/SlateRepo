package com.slate.asynctask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.slate.activities.SlateActivity;
import com.slate.interfaces.CheckUserAsyncResponse;

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
public class CheckUserAsyncTask extends AsyncTask<Void,Void,Void>{

    String android_id;
    String resultUserId;
    String resultRegistrationId;
    boolean isUserAvailable;
    Context mContext;
    public CheckUserAsyncResponse delegate;

    public CheckUserAsyncTask(String android_id, Context mContext)
    {
        this.android_id = android_id;
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder builder = null;
        try {
            String urlStr= "https://slate-muzak.rhcloud.com/checkUser.php?android_id="+android_id+"";
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
            JSONArray jsonArray = new JSONArray(builder.toString());
            Log.i("Slate:CheckUser", "JSON :" + jsonArray.toString());

            // If length is 0 -> User not present. So set (isUserAvailable = false). and call addUser
            // Else ->User present. So set (isUserAvailable = true). and call getSlateItems
            // Get userId and fill into this.resultUserId

            if(jsonArray.length()==0)
            {
                this.isUserAvailable = false;
            }
            else
            {
                this.isUserAvailable=true;
                JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                this.resultUserId = jsonObject.get("ID").toString();
                this.resultRegistrationId = jsonObject.get("RegID").toString();
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

        if(isUserAvailable)
        {
            // Application has mostly been uninstalled in this case, so refresh the RegistrationId
            //TODO: Check if this.resultRegistrationId is not empty
            delegate.updateRegistrationId(this.resultUserId);
            //delegate.saveToSharedPreferences(this.resultUserId, this.resultRegistrationId);
            delegate.goToSlateScreen();
        }
        else{
            delegate.goToAddUserScreen();
        }


    }
}

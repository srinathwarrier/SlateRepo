package com.slate1.asynctask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.slate1.activities.SlateActivity;
import com.slate1.interfaces.SuggestionAsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by I076630 on 06-May-15.
 */
public class GetSuggestionsAsyncTask extends AsyncTask<Void,Void,Document>{

    String suggestionString;
    Context mContext;
    String resultUserId;
    public SuggestionAsyncResponse delegate;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayAdapter<String> mAdapter;

    public GetSuggestionsAsyncTask(String suggestionString, Context mContext , ArrayAdapter<String> mAdapter, ArrayList<String> suggestionArrayList)
    {
        this.suggestionString = suggestionString;
        this.mContext = mContext;
        this.mAdapter=mAdapter;
        this.stringArrayList=suggestionArrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        stringArrayList.clear();
        this.mAdapter.notifyDataSetChanged();
        Log.i("AsyncTask", "onPreExecute");
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            String urlStr="https://suggestqueries.google.com/complete/search?client=toolbar&ds=yt&q="+this.suggestionString;
            URL url = new URL(urlStr);

            URI uri = new URI(
                    "https",
                    "suggestqueries.google.com",
                    "/complete/search",
                    "client=toolbar&ds=yt&q="+this.suggestionString,
                    null);
            String request = uri.toASCIIString();
            URL url2 = new URL(request);



            URLConnection connection = url2.openConnection();

            Document doc = parseXML(connection.getInputStream());
            return doc;


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
    protected void onPostExecute(Document doc) {
        super.onPostExecute(doc);

        try {
            NodeList descNodes = doc.getElementsByTagName("suggestion");

            for (int i = 0; i < descNodes.getLength(); i++) {
                String temp = descNodes.item(i).getAttributes().getNamedItem("data").getNodeValue();
                stringArrayList.add(temp);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }


    private Document parseXML(InputStream stream)
            throws Exception
    {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try
        {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex)
        {
            throw ex;
        }

        return doc;
    }
}

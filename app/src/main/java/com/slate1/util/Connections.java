package com.slate1.util;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by i076324 on 5/31/2015.
 */
public class Connections {
    //public String getSlateItemsURL = "https://slate-muzak.rhcloud.com/getSlateItems.php?id=";
    public static String ipAddress  = "ec2-52-25-222-182.us-west-2.compute.amazonaws.com";
    boolean isLiveSystem=false;
    private String systemName="slate";
    String versionName="v1";
    public Connections(){
        if(!isLiveSystem){
            setSystemName("beta");
        }

    }

    public String getSlateItemsURL(String userId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    "/"+ getSystemName() +"/"+versionName+"/"+"getSlateItems.php", // "/slate/v1/getSlateItems.php",
                    "id=" + userId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;//"http://52.24.136.48/slate/v1/getSlateItems.php?id="+userId;
    }



    public String getTalksURL(String userSongId,String userId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    "/"+ getSystemName() +"/"+versionName+"/"+"getComment.php", // "/slate/v1/getComment.php",
                    "usersongid=" + userSongId +"&userid=" + userId ,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }




    public String getSongURL(String suggestionString){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    "/"+ getSystemName() +"/"+versionName+"/"+"suggestion.php",
                    "suggestion="+suggestionString,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }




    public String getAddSongURL(String userId,String query, String youtubeLinkString){
        Map<String,String> parametersMap = new HashMap<String,String>();
        parametersMap.put("id",userId);
        parametersMap.put("song_name",query);
        parametersMap.put("url",youtubeLinkString);
        String request=constructURLString("addSong.php",parametersMap);
        return request;
    }

    public String getAddTalkURL(String userId, String userSongId, String talkText){
        Map<String,String> parametersMap = new HashMap<String,String>();
        parametersMap.put("id", userId);
        parametersMap.put("usersongid",userSongId);
        parametersMap.put("text",talkText);
        String request=constructURLString("insertComment.php",parametersMap);
        return request;
    }

    public String getAddUserURL(String androidId, String userName, String userEmail, String registrationId){
        Map<String,String> parametersMap = new HashMap<String,String>();
        parametersMap.put("android_id", androidId);
        parametersMap.put("name",userName);
        parametersMap.put("email",userEmail);
        parametersMap.put("reg_id",registrationId);
        String request=constructURLString("createNewUser.php",parametersMap);
        return request;
    }

    public String getCheckUserURL(String androidId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    "/"+ getSystemName() +"/"+versionName+"/"+"checkUser.php", // "/slate/v1/checkUser.php",
                    "android_id=" + androidId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }

    public String getUpdateRegIdURL(String userId,String registrationId){
        String request="";
        try {
            URI uri= new URI(
                    "http",
                    ipAddress,
                    "/"+ getSystemName() +"/"+versionName+"/"+"updateRegID.php",
                    "id="+userId+"reg_id="+registrationId,
                    null);
            request = uri.toASCIIString();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return request;
    }


    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }


    public String getSystemNameCamelCase() {
        if(systemName.equals("beta")) return "Beta";
        else return "Slate";
    }

    public String replaceAmpersandInString(String queryString){
        return queryString.replace("&","%26");
    }

    public String constructURLString (String phpFileName, Map<String, String> parametersMap){
        // For addSong :
        // http://ec2-52-24-136-48.us-west-2.compute.amazonaws.com/beta/v1/addSong.php?
        // id=22
        // &song_name=Chup,%20Zeb%20%26%20Haniya,%20Coke%20Studio%20Pakistan,%20Season%202&
        // url=https://www.youtube.com/watch?v=3XAO-IqPcn4

        String resultString = "http://"+
                ipAddress+
                "/"+ getSystemName() +"/"+versionName+"/"+phpFileName;
        if(parametersMap.size()>0)resultString+="?";
        for(Map.Entry<String,String> entry : parametersMap.entrySet()){
            String encodedValue = Uri.encode(entry.getValue());

            resultString+=
                    entry.getKey() +
                    "="+
                    encodedValue+
                    "&";
        }
        if((resultString.substring(resultString.length() - 1)).equals("&")){
            resultString = resultString.substring(0, resultString.length()-1);
        }
        return resultString;
    }

}

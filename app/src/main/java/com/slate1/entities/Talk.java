package com.slate1.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by I076630 on 06-May-15.
 */
public class Talk {
    private String talkText;
    private String name;
    private String dateAdded;

    public String getDateAdded() {
        return dateAdded;
    }

    //Newly added
    public String getDateAddedFormattedString() {
        String formattedString="";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateAdded);
            sdf = new SimpleDateFormat("EEE, MMM d, yyyy, h:mm a");
            formattedString = sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return formattedString;
    }



    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }


    public String getTalkText() {
        return talkText;
    }

    public void setTalkText(String talkText) {
        this.talkText = talkText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

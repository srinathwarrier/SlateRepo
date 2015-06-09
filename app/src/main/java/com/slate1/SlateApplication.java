package com.slate1;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.slate1.entities.NotificationMessage;

import java.util.ArrayList;

/**
 * Created by i076324 on 6/8/2015.
 */
public class SlateApplication  extends Application {
    // model
    private static int numUnreadMessages;
    private static NotificationCompat.InboxStyle inboxStyle;
    private static int notificationStatus =0; // 1: only ADD_SONG 2: only TALK_SONG 3 : both
    public static final int NOTIFICATION_ID = 1;

    public NotificationCompat.InboxStyle getInboxStyle() {
        return inboxStyle;
    }

    public void setInboxStyle(NotificationCompat.InboxStyle inboxStyle) {
        this.inboxStyle = inboxStyle;
    }

    public int getNumUnreadMessages() {
        return numUnreadMessages;
    }

    public void setNumUnreadMessages(int numUnreadMessages) {
        this.numUnreadMessages = numUnreadMessages;
    }



    public static int addNotification(NotificationMessage notificationMessage){
        numUnreadMessages++;

        if(notificationMessage.getNotificationType() == NotificationType.ADD_SONG){
            if(notificationStatus ==0){
                notificationStatus = 1;
            }
            else if(notificationStatus ==2){
                notificationStatus =3;
            }
        }
        else if (notificationMessage.getNotificationType() == NotificationType.TALK_SONG){
            if(notificationStatus ==0){
                notificationStatus = 2;
            }
            else if(notificationStatus ==1){
                notificationStatus =3;
            }
        }
        return notificationStatus;
    }

    public void removeAllNotifications(){
        setNumUnreadMessages(0);
        setInboxStyle(new NotificationCompat.InboxStyle());
        notificationStatus =0;

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public enum NotificationType{
        ADD_SONG , TALK_SONG;
    }
}

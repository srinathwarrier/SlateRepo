package com.slate1;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.slate1.activities.SlateActivity;
import com.slate1.entities.NotificationMessage;

import java.util.ArrayList;

/**
 * Created by I076324 on 5/13/2015.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = SlateApplication.NOTIFICATION_ID;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    static final String TAG = "GCMDemo";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String notificationTypeString = intent.getStringExtra("type");
                String displayMessage = intent.getStringExtra("message");
                String userSongId = intent.getStringExtra("usersongid");

                int notificationTypeInt ;
                if(notificationTypeString != null){
                    notificationTypeInt = Integer.parseInt(notificationTypeString);
                }
                else{
                    //TODO: Remove this as well
                    if(displayMessage.toLowerCase().contains(("has added").toLowerCase())){
                        notificationTypeInt = 1;
                        userSongId ="";
                    }
                    else{// if(displayMessage.toLowerCase().contains(("has talked").toLowerCase())){
                        notificationTypeInt = 2;
                        userSongId = "278";
                    }
                }

                NotificationMessage notificationMessage = new NotificationMessage(notificationTypeInt , displayMessage,userSongId );

                sendNotification(notificationMessage);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(NotificationMessage notificationMessage) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, SlateActivity.class);


        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("IS_NOTIFICATION", true);
        intent.putExtra("NOTIFICATION_TYPE", notificationMessage.getNotificationType());
        intent.putExtra("USER_SONG_ID", notificationMessage.getUserSongId());


        SlateApplication application = (SlateApplication) getApplication();
        int notificationStatus = SlateApplication.addNotification(notificationMessage);
        int numMessages = application.getNumUnreadMessages();


        NotificationCompat.InboxStyle inboxStyle = application.getInboxStyle();
        if(inboxStyle==null){
            application.setInboxStyle(new NotificationCompat.InboxStyle());
            inboxStyle = application.getInboxStyle();
        }
        inboxStyle.addLine(notificationMessage.getDisplayMessage());

        String contextText ="Check these out !";
        String contentTitle = "Slate";
        if(numMessages==1){
            // For SINGLE notification
            contentTitle = "1 new notification";
            contextText =notificationMessage.getDisplayMessage();
        }
        else{
            // For more than 1 notifications
            contentTitle = numMessages+" new notifications";
            contextText = numMessages+" notifications";
            if(notificationStatus ==1){ // 1: only ADD_SONG
                contextText =  numMessages +" songs have been added";
            }
            else if(notificationStatus ==2){// 2: only TALK_SONG
                contextText = numMessages+" new talks";
            }
            else if(notificationStatus ==3){ // 3 : both
                contextText = "New songs, New talks";
            }
        }




        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(contentTitle)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setStyle(inboxStyle)
                        .setContentText(contextText);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    //TODO: This is the default method. Remove this method.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SlateActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Slate")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
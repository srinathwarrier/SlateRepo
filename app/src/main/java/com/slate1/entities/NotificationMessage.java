package com.slate1.entities;

import com.slate1.SlateApplication;

/**
 * Created by i076324 on 6/8/2015.
 */
public class NotificationMessage {
    private SlateApplication.NotificationType notificationType; // 1: AddSong 2: AddTalk
    private String displayMessage;
    private String userSongId;

    public NotificationMessage(int notificationTypeInt, String displayMessage, String userSongId) {
        this.setNotificationType(getNotificationTypeFromIntValue(notificationTypeInt));
        this.displayMessage = displayMessage;
        this.userSongId = userSongId;
    }

    public SlateApplication.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(SlateApplication.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public SlateApplication.NotificationType getNotificationTypeFromIntValue(int notificationType) {
        if(notificationType==1){
            return SlateApplication.NotificationType.ADD_SONG;
        }
        else { //if(notificationType==2)
            return SlateApplication.NotificationType.TALK_SONG;
        }
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getUserSongId() {
        return userSongId;
    }

    public void setUserSongId(String userSongId) {
        this.userSongId = userSongId;
    }

}

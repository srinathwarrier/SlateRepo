package com.slate1.entities;

/**
 * Created by I076630 on 06-May-15.
 */
public class Song {
    private String songID;
    private String frequency;
    private String dateAdded;
    private String songDescription;
    private String friendName;
    private String isUnreadStatus;
    private String youtubeLink;

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSongDescription() {
        return songDescription;
    }

    public void setSongDescription(String songDescription) {
        this.songDescription = songDescription;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getIsUnreadStatus() {
        return isUnreadStatus;
    }

    public void setIsUnreadStatus(String isUnreadStatus) {
        this.isUnreadStatus = isUnreadStatus;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}

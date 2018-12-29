package edu.unipi.students.omadara.musicplayer.models;

import android.graphics.Bitmap;

public class Album {
    private String id;
    private String title;
    private String artist;
    private int trackCount;
    private int duration;
    private Bitmap thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Album{id='" + getId() + "', title='" + getTitle() + "', artist='" + getArtist()
                + "', trackCount='" + getTrackCount() + "', duration='" + getDuration() + "', thumbnail='"+getThumbnail()+"'}";
    }
}

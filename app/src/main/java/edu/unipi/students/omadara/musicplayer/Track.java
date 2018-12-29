package edu.unipi.students.omadara.musicplayer;

public class Track {
    private String name;
    private int duration;
    private String mediaUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public String toString() {
        return "Track{name='" + getName() + "', duration='" + getDuration() + "', mediaUrl='" + getMediaUrl() + "'}";
    }
}

package edu.unipi.students.omadara.musicplayer;

public class Album {
    private String title;
    private String artist;
    private String duration;
    private String songs_cnt;
    private int image; //TODO bitmap instead of id

    public Album() {
        ;;
    }

    public Album(String title, String artist, String duration, String songs_cnt, int image) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.songs_cnt = songs_cnt;
        this.image = image;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSongs_cnt() {
        return songs_cnt;
    }

    public void setSongs_cnt(String songs_cnt) {
        this.songs_cnt = songs_cnt;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}

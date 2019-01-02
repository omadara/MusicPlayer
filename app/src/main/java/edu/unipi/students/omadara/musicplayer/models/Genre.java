package edu.unipi.students.omadara.musicplayer.models;

import android.graphics.Bitmap;

public class Genre {
    private String id;
    private String name;
    private Bitmap thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Genre{id='" + getId() + "', name='" + getName() + "', thumbnail='" + getThumbnail() + "'}";
    }
}

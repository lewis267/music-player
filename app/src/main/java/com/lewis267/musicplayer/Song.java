package com.lewis267.musicplayer;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.List;

class Song implements Comparable {
    private long id;
    private String title;
    private String artist;
    private String album;
    private int track;
    private Time playTime;
    private List<String> paths;
    private String art;

    Song (long songID, String songTitle, String songArtist, String albumName, String albumArt) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        album=albumName;
        art=albumArt;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist() {return artist;}
    public String getAlbum() {return album;}
    public int getTrackNum() {return track;}
    public Time getPlayTime() {return playTime;}
    public List<String> getPaths() {return paths;}
    public String getArt() {return art;}

    @Override
    public int compareTo(@NonNull Object o) {
        return this.getTitle().compareTo(((Song)o).getTitle());
    }
}

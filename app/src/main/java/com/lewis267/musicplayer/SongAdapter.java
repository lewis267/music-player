package com.lewis267.musicplayer;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertedView, ViewGroup parent) {

        if (convertedView == null) {

        }
        //map to song layout
        LinearLayout songLay = (LinearLayout) songInf.inflate
                (R.layout.song, parent, false);

        //get title and artist views
        TextView songView = songLay.findViewById(R.id.song_title);
        TextView artistView = songLay.findViewById(R.id.song_artist);
        ImageView artView = songLay.findViewById(R.id.song_art);

        //get song using position
        Song currSong = songs.get(position);

        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());

        // get the image and set it
        String albumArt = currSong.getArt();
        artView.setImageBitmap(BitmapFactory.decodeFile(albumArt));

        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
}

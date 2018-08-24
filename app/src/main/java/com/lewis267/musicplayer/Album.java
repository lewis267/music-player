package com.lewis267.musicplayer;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Album implements Iterable<Song> {

    /**
     * The internal list of songs this album is composed of.
     */
    private final List<Song> songs = new ArrayList<>();
    /**
     * The name of this album's artist.
     */
    private String albumArtist;
    /**
     * The name of this album.
     */
    private String albumName;

    /**
     * Construct a new album.
     * @param name The name of this album.
     * @param artist The name of the artist of this album.
     * @param songs The iterable collection of songs to compose
     *              this album.
     */
    public Album(String name, String artist, Iterable<Song> songs) {
        this.albumName = name;
        this.albumArtist = artist;

        // Copy over the songs from the iterable. (iterable may block)
        songs.iterator().forEachRemaining(this.songs::add);

        // Sort the songs.
        this.songs.sort(Song::compareTo);
    }

    /**
     * Obtain the artist of the album.
     * @return A string.
     */
    public String getAlbumArtist() { return albumArtist; }

    /**
     * Obtain the name of this album.
     * @return A string.
     */
    public String getAlbumName() { return albumName; }

    /**
     * Obtains the iterator for this album.
     * @return An iterator over the songs in this album.
     */
    @NonNull
    @Override
    public Iterator<Song> iterator() {
        return this.songs.iterator();
    }
}

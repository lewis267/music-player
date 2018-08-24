package com.lewis267.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class to hold the collection of music
 * available on the host device.
 */
public class MusicLibrary implements Iterable<Song> {

    //region --- Private Variables ---

    /**
     * The internal list of songs which constitute this
     * class.
     */
    private final List<Song> Songs = new ArrayList<>();

    /**
     * The internal list of albums contained on the host
     * device.
     */
    private final List<Album> Albums = new ArrayList<>();

    /**
     * The context of this class based in construction.
     */
    private Context LibraryContext;

    //endregion

    /**
     * Initialize this class.
     * @param context the context of the app.
     */
    public MusicLibrary(Context context) {
        this.LibraryContext = context;

        if (!this.Synchronize())
            Log.println(Log.WARN, "MusicLibrary", "Synchronization failed in constructor.");
    }

    /**
     * Performs synchronizing actions which ensures that
     * MusicLibrary class is mirroring the contents of
     * the device.
     * @return true for successful mirroring.
     */
    public boolean Synchronize() {

        // TODO read the device's music

        return false;
    }

    /**
     * Obtains the iterator for this library of Songs which
     * iterates over all the songs which are in this
     * MusicLibrary.
     * @return An iterator over the songs.
     */
    @NonNull
    @Override
    public Iterator<Song> iterator() {
        return Songs.iterator();
    }
}

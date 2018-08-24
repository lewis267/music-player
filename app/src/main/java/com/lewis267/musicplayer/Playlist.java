package com.lewis267.musicplayer;

/**
 * Defines an abstract definition of
 * a playlist element.
 */
public abstract class Playlist {

    /**
     * The current track from the playlist.
     */
    public Song CurrentSong;

    /**
     * Obtains the next song in the playlist
     * and moves current forward.
     * @return The new current track.
     */
    public abstract Song shiftNext();

    /**
     * Obtains the last song in the playlist
     * and moves current backwards.
     * @return The new current track.
     */
    public abstract Song shiftPrevious();
}

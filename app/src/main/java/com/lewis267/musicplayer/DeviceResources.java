package com.lewis267.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class DeviceResources {

    /**
     * Obtains a list of songs from the device storage.
     * @return an array list of songs.
     */
    @NonNull
    public static ArrayList<Song> getAllSongsFromDevice(Context context) {
        ArrayList<Song> songs = new ArrayList<>();

        // Column indices
        int titleColumn;
        int idColumn;
        int artistColumn;
        int albumColumn;
        int albumIDColumn;

        ContentResolver musicResolver = context.getContentResolver();
        Cursor musicCursor = musicResolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        if(musicCursor != null) {
            try {
                if (musicCursor.moveToFirst()) {

                    // set columns
                    titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                    albumIDColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

                    // add songs to list
                    do {
                        long thisId = musicCursor.getLong(idColumn);
                        String songTitle = musicCursor.getString(titleColumn);
                        String songArtist = musicCursor.getString(artistColumn);
                        String songAlbum = musicCursor.getString(albumColumn);
                        String songAlbumId = musicCursor.getString(albumIDColumn);
                        String albumArt = getArtworkPath(context, songAlbumId);

                        // add the song
                        songs.add(new Song(thisId, songTitle, songArtist, songAlbum, albumArt));
                    }
                    while (musicCursor.moveToNext());
                }
            }
            finally {
                musicCursor.close();
            }
        }

        return songs;
    }

    /**
     * Obtains the artwork bitmap for a specific song id.
     * @param context The current context.
     * @param albumId The song to query.
     * @return The Bitmap of the album's art. May be null.
     */
    @Nullable
    static Bitmap getArtworkBitmap(Context context, int albumId) {
        try {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
            ContentResolver res = context.getContentResolver();

            InputStream bitmapInputStream;
            try {
                bitmapInputStream = res.openInputStream(uri);
                if (bitmapInputStream != null) {
                    try {
                        return BitmapFactory.decodeStream(bitmapInputStream);
                    } finally {
                        bitmapInputStream.close();
                    }
                }
                else return null;
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        catch (Exception e) {
            Log.println(Log.ERROR, "MusicPlayer AlbumArt IO", e.getMessage());
            return null;
        }
    }

    /**
     * Obtains the path to the artwork for a specified album.
     * @param context The context of the call.
     * @param albumId The identifier for the album.
     * @return A path. May be null.
     */
    @Nullable
    static String getArtworkPath(Context context, String albumId) {
        String albumArt = null;

        try {
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{String.valueOf(albumId)},
                    null);


            if (cursor != null && cursor.moveToFirst()) {
                albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            if (cursor != null) cursor.close();
        }
        catch (Exception e) {
            Log.println(Log.ERROR, "getArtworkPath", e.getMessage());
            return null;
        }

        return albumArt;
    }
}
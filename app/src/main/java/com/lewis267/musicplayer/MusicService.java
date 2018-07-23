package com.lewis267.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener
{

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosition;
    private String songTitle = "";
    private static final int NOTIFY_ID=1;
    private final IBinder musicBind = new MusicBinder();
    private boolean shuffle=false;
    private Random rand;

    public void onCreate(){
        super.onCreate();
        //initialize position
        songPosition =0;
        //create player
        player = new MediaPlayer();

        rand = new Random();

        // setup the player
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    // Set the list of songs for this service to play.
    public void setList(ArrayList<Song> toPlay) {
        songs=toPlay;
    }

    @Override
    public void onAudioFocusChange(int i) {
        pausePlayer();
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(player.getCurrentPosition()>0){
            mediaPlayer.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void playSong() {
        player.reset();

        // get song
        Song playable = songs.get(songPosition);
        // set the title
        songTitle = playable.getTitle();
        // get id
        long currSong = playable.getID();
        // set uri
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public void setSong(int songIndex) {
        songPosition =songIndex;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosition--;
        if(songPosition < 0) songPosition =songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if (shuffle){
            int newSong = songPosition;
            while (newSong== songPosition){
                newSong = rand.nextInt(songs.size());
            }
            songPosition =newSong;
        }
        else {
            songPosition++;
            if (songPosition >= songs.size()) songPosition = 0;
        }
        playSong();
    }

    public void setShuffle(){
        shuffle=!shuffle;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}

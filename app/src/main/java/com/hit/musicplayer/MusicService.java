package com.hit.musicplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;

import java.io.UnsupportedEncodingException;


public class MusicService extends Service implements  MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    final int NOTIFICATION_ID=1;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    boolean isPaused=false;
    PlayList playList;
    RemoteViews remoteViews;
    RemoteViews textView;
    NotificationCompat.Builder builder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            playList =PlayList.getInstance();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.reset();

        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String channelId="channel_id";
        String channelName="Music Channel";

        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(this,channelId);
        remoteViews =new RemoteViews(getPackageName(),R.layout.music_notification);
        textView = new RemoteViews(getPackageName(),R.id.notification_song_title_txt);

        Intent playIntent=new Intent(this,MusicService.class);
        playIntent.putExtra("command","play");
        PendingIntent playPendingIntent = PendingIntent.getService(this,0,playIntent ,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.play_song_notif_btn,playPendingIntent);

        Intent prevIntent=new Intent(this,MusicService.class);
        prevIntent.putExtra("command","prev");
        PendingIntent prevPendingIntent = PendingIntent.getService(this,1,prevIntent ,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.prev_song_notif_btn,prevPendingIntent);

        Intent nextIntent=new Intent(this,MusicService.class);
        nextIntent.putExtra("command","next");
        PendingIntent nextPendingIntent = PendingIntent.getService(this,2,nextIntent ,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_song_notif_btn,nextPendingIntent);

        Intent closeIntent=new Intent(this,MusicService.class);
        closeIntent.putExtra("command","close");
        PendingIntent closePendingIntent = PendingIntent.getService(this,3,closeIntent ,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_song_notif_btn,closePendingIntent);

        Intent pauseIntent=new Intent(this,MusicService.class);
        pauseIntent.putExtra("command","pause");
        PendingIntent pausePendingIntent = PendingIntent.getService(this,4,pauseIntent ,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.pause_song_notif_btn,pausePendingIntent);
        builder.setSilent(true);

        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(android.R.drawable.ic_media_play);
        startForeground(NOTIFICATION_ID,builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command=intent.getStringExtra("command");
        switch (command){
            case "new_instance":
                if(!mediaPlayer.isPlaying()) {
                  playList.setServiceUp(true);
                }
                break;

            case "pause":
                isPaused=true;
                playList.setPlaying(false);
                NotifPlayChanged();
                mediaPlayer.pause();
                break;

            case "play":
                if (playList.getSongs().size()==0){
                    sendErrorMessage();
                }
                else if(isPaused==true){
                    isPaused=false;
                    mediaPlayer.start();
                    playList.setPlaying(true);
                    NotifPlayChanged();
                }
                else if(!mediaPlayer.isPlaying() &&isPaused==false){
                    try {
                        mediaPlayer.setDataSource(playList.getSongs().get(playList.getCurrentlyPlaying()).getSongLink());
                        mediaPlayer.prepareAsync();
                        playList.setPlaying(true);
                        NotifTitleChanged();
                        NotifPlayChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "next":
                if (playList.getSongs().size()==0){
                    sendErrorMessage();
                }
                else if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                playSong(true);
                break;

            case "prev":
                if (playList.getSongs().size()==0){
                    sendErrorMessage();
                }
                else if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                playSong(false);
                break;

            case "close":
                stopSelf();
                playList.setServiceUp(false);
                playList.setPlaying(false);
                NotifPlayChanged();
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendErrorMessage() {
        Toast.makeText(this,"There are no songs in your playlist please load songs",Toast.LENGTH_LONG).show();
    }

    public void NotifTitleChanged(){
        Intent intent = new Intent("com.hit.musicplayer.titlechanged");
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
        remoteViews.setTextViewText(R.id.notification_song_title_txt,playList.getSongs().get(playList.getCurrentlyPlaying()).getSongTitle());
        builder.setCustomContentView(remoteViews);
        builder.setSilent(true);
        builder.setSmallIcon(android.R.drawable.ic_media_play);
        startForeground(NOTIFICATION_ID,builder.build());
    }
    public void NotifPlayChanged(){
        Intent intent = new Intent("com.hit.musicplayer.playchanged");
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!= null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       playSong(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {mediaPlayer.start();

    }
    private void playSong(boolean isNext){
        if(isNext){
            playList.setCurrentlyPlaying(playList.getCurrentlyPlaying()+1);
            if (playList.getCurrentlyPlaying()==playList.getSongs().size())playList.setCurrentlyPlaying(0);
        }
        else {
            playList.setCurrentlyPlaying(playList.getCurrentlyPlaying()-1);
            if (playList.getCurrentlyPlaying()<0)playList.setCurrentlyPlaying(playList.getSongs().size()-1);

        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(playList.getSongs().get(playList.getCurrentlyPlaying()).getSongLink());
            NotifTitleChanged();
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

package com.hit.musicplayer;


import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PlayList {
    private ArrayList<Song> songs;
    private boolean isPlaying = false;
    private boolean isServiceUp = false;
    private int currentlyPlaying = 0;
    private static PlayList playList = null;

    public static PlayList getInstance() throws UnsupportedEncodingException {
        if (playList == null) {
            synchronized (PlayList.class) {
                if (playList == null) {
                    playList = new PlayList();
                }
            }
        }
        return playList;
    }

    private PlayList()  {
        songs = new ArrayList<Song>();

        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/completely_well", "https://drive.google.com/uc?export=download&id=1kVyo_4NwuZqyi7WhCRNx7i-4OeSedP4f","The Thrill Is Gone","BB King","5:26"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/layla", "https://drive.google.com/uc?export=download&id=1X_uV-PFQIsemQ27VNJYJUkrM1YDNq_1v","Layla","Derek & The Dominos","7:11"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/come", "https://drive.google.com/uc?export=download&id=1uOJG8UQcKqk9Uv0_4OndES4IHjCk1vtH","Come Together","The Beatles","4:21"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/day", "https://drive.google.com/uc?export=download&id=1AkHX4R_rX3xYRKTiHx11IO3aRxreZdeR","Day Tripper","The Beatles","2:50"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/wing", "https://drive.google.com/uc?export=download&id=1IJMsXFvqy3hfpPaUoOq_4QFMhYRLl1H-","Little Wing","Jimi Hendrix","2:27"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/buy", "https://drive.google.com/uc?export=download&id=1i8hslo_xFbs5ptCJezxMgBlxuGKPHI0I","Can't Buy Me Love","The Beatles","2:11"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/echoes", "https://drive.google.com/uc?export=download&id=1Sgzn3ewByHCn0AbqXLQAtbHdqAowfRlY","Echoes, Part I","Pink Floyd","11:00"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/paranoid", "https://drive.google.com/uc?export=download&id=1yhKkB5NAaEk6dHdqY8F4RSLFc5tMMbBw","Paranoid","Black Sabbath","2:48"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/traffic", "https://drive.google.com/uc?export=download&id=1bDqCpz9N-wZQchKL9fAUiDyjGhxVBzBX","Crosstown Traffic","Jimi Hendrix","2:25"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/walk", "https://drive.google.com/uc?export=download&id=1YokQpVurCect8A8zt52adjR5Eg_wo9sg","Walk This Way","Aerosmith","3:41"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/wall", "https://drive.google.com/uc?export=download&id=1XAFdD9UHleB5lB7DpMRkN6ebxW2Wfp77","Comfortably Numb","Pink Floyd","6:22"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/traffic", "https://drive.google.com/uc?export=download&id=1GKeycWCRGh_Dt6YmMuEHXBoE4OB15yKV","All Along The Watchtower","Jimi Hendrix","4:01"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/alice", "https://drive.google.com/uc?export=download&id=1s4VUq8eHVDBBgYwnGld62pTx7yIMWvE9","Down In A Hole","Alice In Chains","5:46"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/alice", "https://drive.google.com/uc?export=download&id=1ApY5rZOMsOGD_QoQHorBwNQvNpPzdEFa","Nutshell","Alice In Chains","4:57"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/back", "https://drive.google.com/uc?export=download&id=1PhVqsQ0ETQC143C3sYweFKIEfPt_Ck4p","Back In Black","AC/DC","4:12"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/hotel", "https://drive.google.com/uc?export=download&id=1d26PxpqTWu0c1arKzta1sETd2IvP622T","Hotel California","Eagles","6:32"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/tears", "https://drive.google.com/uc?export=download&id=1Iq_CIvod_3zzx2ugIZC8uDyBzCyQ31P3","Tears In Heaven","Eric Clapton","4:33"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/hell", "https://drive.google.com/uc?export=download&id=14pvsrSIRD6Rs38n9ET1b8uAWzusK3Mlb","Highway To Hell","AC/DC","3:28"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/two", "https://drive.google.com/uc?export=download&id=1S4xgrNSpNanCDQIZLNGwwcFH6VOUUpWI","Ramble On","Led Zeppelin","4:24"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/four", "https://drive.google.com/uc?export=download&id=1WZkSLFlkbyybish1zEAeOQyQLC2sgIEk","Black Dog","Led Zeppelin","4:57"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/two", "https://drive.google.com/uc?export=download&id=199riCItbB2e2vLasnFuRnvO7I42sLJWw","Whole Lotta Love","Led Zeppelin","5:31"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/money", "https://drive.google.com/uc?export=download&id=1WMYf40J13c7VZlDPfJ_FIS5QyXzw_n9M","Money For Nothing","Dire Straits","8:26"));
        songs.add(new Song("android.resource://com.hit.musicplayer/drawable/sultans", "https://drive.google.com/uc?export=download&id=1Mh6tici3wM-haY-lfT6TggiEE8_6a9gq","Sultans Of Swing","Dire Straits","5:51"));


    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(int currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(int position) {
        songs.remove(position);
    }

    public boolean isServiceUp() {
        return isServiceUp;
    }

    public void setServiceUp(boolean serviceUp) {
        isServiceUp = serviceUp;
    }

    public void moveSong(int from, int to) {
        songs.add(to, songs.remove(from));
        if (from == currentlyPlaying) {
            currentlyPlaying = to;
        } else if (to < currentlyPlaying && from > currentlyPlaying) {
            currentlyPlaying++;
        } else if (to > currentlyPlaying && from < currentlyPlaying) {
            currentlyPlaying--;
        }

    }

    void saveSongs(AppCompatActivity activity){
        try {
            FileOutputStream outputStream = activity.openFileOutput("playlist", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(songs);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void loadSongs(AppCompatActivity activity){
        try {
            FileInputStream inputStream=activity.openFileInput("playlist");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<Song> songs=(ArrayList<Song>)objectInputStream.readObject();
            if(songs!=null) {
                this.songs=songs;
            }
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {

        }
    }
}

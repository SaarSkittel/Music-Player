package com.hit.musicplayer;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Song implements Serializable {
    private String image;
    private String songLink;
    private String songTitle;
    private String songTime;
    private String songName;
    private String songArtist;

    public Song(String image, String songLink,String songName,String songArtist,String songTime){
        if (image != null) {
            this.image = image;
        } else {
            this.image = "android.resource://com.hit.musicplayer/drawable/default_image";
        }
        this.songLink=songLink;
        this.songName=songName;
        this.songArtist=songArtist;
        this.songTime=songTime;
        setSongTitle();
    }

    public Song(String image, String songLink) throws UnsupportedEncodingException {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                retriever.setDataSource(songLink, new HashMap<String, String>());
            } catch (RuntimeException ex) {
                throw new UnsupportedEncodingException("error");
            }
        } else {
            retriever.setDataSource(songLink);
        }

        if (image != null) {
            this.image = image;
        } else {
            this.image = "android.resource://com.hit.musicplayer/drawable/default_image";
        }

        songTitle = new String();
        songName=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        songArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        songTitle = songName + "\n" + songArtist;
        long time = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Integer dec = (int) ((time / 1000) % 60);
        songTime = ((time / 1000) / 60) + ":" + ((time / 1000) % 60);
        if (dec <= 9) {
            songTime = songTime + 0;
        }
        this.songLink = songLink;

    }
    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle() {
        this.songTitle = songTitle = songName + "\n" + songArtist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) throws UnsupportedEncodingException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                retriever.setDataSource(songLink, new HashMap<String, String>());
            } catch (RuntimeException ex) {
                throw new UnsupportedEncodingException("error");
            }
        } else {
            retriever.setDataSource(songLink);
        }

        this.songTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) + "\n" + retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        this.songLink = songLink;
        long time = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Integer dec = (int) ((time / 1000) % 60);
        this.songTime = ((time / 1000) / 60) + ":" + ((time / 1000) % 60);
        if (dec <= 9) {
            songTime = songTime + 0;
        }

    }

    public String getSongTime() {
        return songTime;
    }




}

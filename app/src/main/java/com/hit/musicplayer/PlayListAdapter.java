package com.hit.musicplayer;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.SongViewHolder> {
    public List<Song>songs;
    private MyPlayListListener listener;

    interface MyPlayListListener{
        void onSongClicked(int position,View view);
    }

    public void setListener(MyPlayListListener listener) {
        this.listener = listener;
    }

    public PlayListAdapter(List<Song>songs){ this.songs=songs; }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        ImageView songImage;
        TextView songTitle;
        TextView timeSong;


        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            this.songImage = itemView.findViewById(R.id.song_image);
            this.songTitle = itemView.findViewById(R.id.song_name);
            this.timeSong = itemView.findViewById(R.id.time_song);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(listener!=null){
                       listener.onSongClicked(getAdapterPosition(),v);
                   }
               }
           });
        }
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_cell,parent,false);
        SongViewHolder songViewHolder=new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song =songs.get(position);
       //Glide.with(holder.itemView.getRootView()).load(Uri.parse(song.getImage())).into(holder.songImage);
        holder.songImage.setImageURI(Uri.parse(song.getImage()));
        holder.songTitle.setText(song.getSongTitle());
        holder.timeSong.setText(song.getSongTime());
    }




    @Override
    public int getItemCount() {
        return songs.size();
    }
}

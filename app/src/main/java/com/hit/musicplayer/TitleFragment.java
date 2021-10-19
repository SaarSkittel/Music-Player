package com.hit.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

public class TitleFragment extends Fragment {
    int position;
    PlayList playList;
    EditText songName_et;
    EditText songArtist_et;
    Button change_btn;
    String songName;
    String songArtist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.title_fragment,container,false);

        try {
            playList=PlayList.getInstance();
        } catch (UnsupportedEncodingException e) {

        }

        position=requireArguments().getInt("position",0);
        songName =playList.getSongs().get(position).getSongName();
        songName_et = view.findViewById(R.id.change_song_et);
        songName_et.setHint(songName);

        songArtist=playList.getSongs().get(position).getSongArtist();
        songArtist_et=view.findViewById(R.id.change_artist_et);
        songArtist_et.setHint(songArtist);

        change_btn=view.findViewById(R.id.change_btn);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!songArtist_et.getText().toString().isEmpty()){
                    playList.getSongs().get(position).setSongArtist(songArtist_et.getText().toString());
                }
                if(!songName_et.getText().toString().isEmpty()){
                    playList.getSongs().get(position).setSongName(songName_et.getText().toString());
                }
                playList.getSongs().get(position).setSongTitle();

            }
        });
        return view;

    }
}
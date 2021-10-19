package com.hit.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class AddSongFragment extends Fragment {
    EditText url_et;

    Button addSong_btn;
    String path=new String();
    SharedPreferences sp;
    PlayList playList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_song_fragment,container,false);
        try {
            playList =PlayList.getInstance();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        path = "android.resource://com.hit.musicplayer/drawable/default_image";
        sp=getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("path_pic",path);
        editor.commit();
        url_et=view.findViewById(R.id.add_song_url_et);


        addSong_btn=view.findViewById(R.id.add_song_btn);
        addSong_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSong_btn.setClickable(false);
                if(url_et.getText().toString().isEmpty()){
                    Toast.makeText(v.getContext(),"Can't open empty URL",Toast.LENGTH_LONG).show();
                    addSong_btn.setClickable(true);
                }
                else {
                    try {
                        PlayList.getInstance().addSong(new Song(path,url_et.getText().toString()));
                        editor.putInt("position",PlayList.getInstance().getSongs().size()-1);
                        editor.commit();
                        Bundle bundle = new Bundle();
                        bundle.putString("source","AddSongFragment");
                        bundle.putInt("position",PlayList.getInstance().getSongs().size()-1);
                        Navigation.findNavController(v).navigate(R.id.action_addSongFragment_to_songFragment,bundle);

                    } catch (UnsupportedEncodingException e) {
                        addSong_btn.setClickable(true);
                        Toast.makeText(view.getContext(),"URL error",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        return view;
    }


}
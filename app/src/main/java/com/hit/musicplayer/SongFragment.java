package com.hit.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;

public class SongFragment extends Fragment {
    int position;
    ImageView songPic;
    TextView songTitle;
    TextView songTime;
    Button changeButton;
    Button changeTitle;
    PlayList playList;
    String url;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.song_fragment,container,false);
        position=requireArguments().getInt("position",0);

        try {
            playList=PlayList.getInstance();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url=playList.getSongs().get(position).getImage();
        sp=getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("source","SongActivity");
        editor.putString("path_pic",url);
        editor.commit();
        songPic=view.findViewById(R.id.image_info);
        Glide.with(this).load(Uri.parse(url)).into(songPic);
        //songPic.setImageURI(Uri.parse(url));
        songTitle=view.findViewById(R.id.song_name_info);
        songTitle.setText(playList.getSongs().get(position).getSongTitle());

        songTime=view.findViewById(R.id.time_info);
        songTime.setText(playList.getSongs().get(position).getSongTime());
        changeButton=view.findViewById(R.id.change_song_picture_btn);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //Intent intent=new Intent(SongFragment.this, PictureFragment.class);
                //bundle.putString("image",url);
                bundle.putInt("position",position);
                bundle.putString("source",requireArguments().getString("source"));
               // startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.action_songFragment_to_pictureFragment,bundle);
            }
        });
        changeTitle=view.findViewById(R.id.change_song_title_btn);
        changeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //Intent intent =new Intent(SongFragment.this,TitleActivity.class);
                bundle.putInt("position",position);
                //startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.action_songFragment_to_titleFragment,bundle);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        url =sp.getString("path_pic",url);
        Glide.with(this).load(Uri.parse(url)).into(songPic);
        songTitle.setText(playList.getSongs().get(position).getSongTitle());
    }

}
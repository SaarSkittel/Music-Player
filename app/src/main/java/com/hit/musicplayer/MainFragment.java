package com.hit.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;

public class MainFragment extends Fragment {
    BroadcastReceiver receiverTitleChanged;
    BroadcastReceiver receiverPlayChanged;
    RecyclerView playList_rv;
    TextView playing_now;
    FloatingActionButton play;
    FloatingActionButton prev;
    FloatingActionButton next;
    FloatingActionButton add;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    PlayListAdapter adapter;
    PlayList playList;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment,container,false);
        playing_now=view.findViewById(R.id.plaing_now_TV);
        try {
            playList = PlayList.getInstance();
            playList.loadSongs((AppCompatActivity) getActivity());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sp = getActivity().getSharedPreferences("preferences",Context.MODE_PRIVATE);
        editor=sp.edit();
        editor.putString("source","MainActivity");
        editor.commit();
        Intent intent = new Intent(view.getContext(),MusicService.class);
        intent.putExtra("command","new_instance");
        getActivity().startService(intent);

        playList_rv =view.findViewById(R.id.play_list_RV);
        playList_rv.setHasFixedSize(true);
        playList_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new PlayListAdapter(playList.getSongs());
        adapter.setListener(new PlayListAdapter.MyPlayListListener() {
            @Override
            public void onSongClicked(int position, View view) {
                Bundle bundle = new Bundle();
                bundle.putString("image",playList.getSongs().get(position).getImage());
                bundle.putInt("position",position);
                bundle.putString("source","MainFragment");
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_songFragment,bundle);
            }
        });


        ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                playList.moveSong(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                playList.saveSongs((AppCompatActivity) getActivity());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT||direction==ItemTouchHelper.LEFT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View dialog = getLayoutInflater().inflate(R.layout.delete_dialog_fragment,null);
                    builder.setView(dialog).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            playList.removeSong(viewHolder.getAdapterPosition());
                            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            playList.saveSongs((AppCompatActivity) getActivity());
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(playList_rv);

        playList_rv.setAdapter(adapter);
        play=view.findViewById(R.id.play_song_btn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MusicService.class);
                if(!playList.isPlaying()) {
                    if(!playList.isServiceUp()){
                        intent.putExtra("command","new_instance");
                        getActivity().startService(intent);
                    }
                    intent.putExtra("command", "play");
                }
                else {
                    intent.putExtra("command", "pause");
                }
                playButtonState();
                getActivity().startService(intent);
            }
        });

        next= view.findViewById(R.id.next_song_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MusicService.class);
                intent.putExtra("command","next");
                getActivity().startService(intent);
            }
        });
        prev=view.findViewById(R.id.prev_song_btn);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MusicService.class);
                intent.putExtra("command","prev");
                getActivity().startService(intent);
            }
        });

        add=view.findViewById(R.id.add_song_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
               Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addSongFragment,bundle);
            }
        });
        IntentFilter filterTitleChanged=new IntentFilter("com.hit.musicplayer.titlechanged");
        receiverTitleChanged = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                playing_now.setText(playList.getSongs().get(playList.getCurrentlyPlaying()).getSongTitle());
            }
        };
        playButtonState();
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(receiverTitleChanged,filterTitleChanged);

        IntentFilter filterPlayChanged = new IntentFilter("com.hit.musicplayer.playchanged");
        receiverPlayChanged =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                playButtonState();
            }
        };
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(receiverPlayChanged,filterPlayChanged);
        adapter.notifyDataSetChanged();
        return view;
    }
    private void playButtonState(){
        if(!playList.isPlaying()) {
            play.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            play.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        playButtonState();
        if(sp.getString("source","MainActivity").matches("AddSongActivity")){
            int position=sp.getInt("position",0);
            Uri uri = Uri.parse(sp.getString("path_pic",null));
            playList.getSongs().get(position).setImage(uri.toString());
            adapter.songs=playList.getSongs();
            adapter.notifyDataSetChanged();
        }
        else {

            adapter.notifyDataSetChanged();
        }
        playList.saveSongs((AppCompatActivity) getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       playList.saveSongs((AppCompatActivity) getActivity());
    }

}
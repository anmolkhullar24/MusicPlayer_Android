package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class player extends AppCompatActivity {

    Button previous , pause , next,repeat,repeatOneButton;
    TextView songNameText;
    SeekBar seek;
    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    String sname;
    Boolean repeatMode;
    Boolean repeatOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        previous =findViewById(R.id.previous);
        pause= findViewById(R.id.pause);
        next = findViewById(R.id.next);
        seek = findViewById(R.id.seekBar);
        repeat = findViewById(R.id.repeat);
        songNameText = findViewById(R.id.songName);
        repeatMode = Boolean.FALSE;
        repeatOne = Boolean.FALSE;
        repeatOneButton  = findViewById(R.id.repeatsong);

        /* updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition<totalDuration){
                    try {
                        sleep(1000);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        seek.setProgress(currentPosition);
                    }
                    catch (InterruptedException e ){

                    }

                }
            }
        };*/
        if(myMediaPlayer!=null)
        {
         myMediaPlayer.stop();
         myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        position = bundle.getInt("pos");
       // sname = mySongs.get(position).toString();

        final String  songName = i.getStringExtra("songName");

        songNameText.setText(songName);
        songNameText.setSelected(true);

        Uri u = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        seek.setMax(myMediaPlayer.getDuration());

        //updateSeekBar.start();
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // seek.setMax(myMediaPlayer.getDuration());
                if(myMediaPlayer.isPlaying()){
                myMediaPlayer.pause();
                pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                }
                else{
                    pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    myMediaPlayer.start();
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = (position+1)%(mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                songNameText.setText(sname);
                myMediaPlayer.start();
                seek.setMax(myMediaPlayer.getDuration());
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position - 1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                sname = mySongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                songNameText.setText(sname);
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                myMediaPlayer.start();
                seek.setMax(myMediaPlayer.getDuration());
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatMode) {
                    repeat.setBackgroundResource(R.drawable.ic_repeat_black_24dp);
                    repeatMode = Boolean.FALSE;
                }
                else{
                repeatMode = Boolean.TRUE;
                repeat.setBackgroundResource(R.drawable.ic_repeat_black_selected_24dp);
                repeatOne = Boolean.FALSE;
                repeatOneButton.setBackgroundResource(R.drawable.ic_repeat_one_black_24dp);
                }
            }
        });

        repeatOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatOne) {
                    repeatOne = Boolean.FALSE;
                    repeatOneButton.setBackgroundResource(R.drawable.ic_repeat_one_black_24dp);
                }
                else{
                    repeatMode = Boolean.FALSE;
                    repeat.setBackgroundResource(R.drawable.ic_repeat_black_24dp);
                    repeatOne = Boolean.TRUE;
                    repeatOneButton.setBackgroundResource(R.drawable.ic_repeat_one_black_selected_24dp);
                }
            }
        });
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(!repeatMode && !repeatOne){
                pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                seek.setProgress(0);
                }
                else if (!repeatMode && repeatOne){
                    myMediaPlayer.start();
                    seek.setProgress(0);
                    seek.setMax(myMediaPlayer.getDuration());
                }
                else if(repeatMode && !repeatOne){
                    myMediaPlayer.stop();
                    myMediaPlayer.release();
                    position = (position+1)%(mySongs.size());
                    Uri u = Uri.parse(mySongs.get(position).toString());
                    myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                    sname = mySongs.get(position).getName().toString().replace(".mp3","").replace(".wav","");
                    songNameText.setText(sname);
                    myMediaPlayer.start();
                    seek.setMax(myMediaPlayer.getDuration());
                }
            }
        });

    }
}

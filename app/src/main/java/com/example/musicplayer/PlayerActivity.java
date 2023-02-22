package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View.OnTouchListener;
import android.os.Bundle;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class PlayerActivity extends AppCompatActivity{

    Bundle songExtraData;
    ImageView prev, play, next, next10, prev10, repeat, random;
    int pos, check = 0;
    static MediaPlayer mediaPlayer;
    TextView songName, timer, maxTime;
    ArrayList<File> musicList;
    RelativeLayout rlMain;
    float x1, x2, y1, y2;
    SeekBar seekBar;
    Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        rlMain = findViewById(R.id.Main);
        prev = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        next10 = findViewById(R.id.next10);
        prev10 = findViewById(R.id.prev10);
        timer = findViewById(R.id.timer);
        repeat = findViewById(R.id.repeat);
        random = findViewById(R.id.random);
        seekBar = findViewById(R.id.seek_bar);
        songName = findViewById(R.id.songName);
        maxTime = findViewById(R.id.maxTime);
        handler = new Handler();


        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Intent intent = getIntent();
        songExtraData = intent.getExtras();

        musicList = (ArrayList) songExtraData.getParcelableArrayList("song");
        pos = songExtraData.getInt("position", 0);

        String name = musicList.get(pos).getName();
        songName.setText(name);

        Uri uri = Uri.parse(musicList.get(pos).toString());
        mediaPlayer = MediaPlayer.create(this, uri);



        OnClickListener oclBtnPlay = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);

                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                    updateSeekBar();



                }


            }
        };

        play.setOnClickListener(oclBtnPlay);


        OnClickListener oclBtnNext = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 0){
                if (pos < musicList.size() - 1) {
                    pos++;
                } else {
                    pos = 0;
                }
                }
                else{
                    pos = (int) (Math.random() * musicList.size());
                }
                initializeMusicPlayer(pos);
            }
        };

        next.setOnClickListener(oclBtnNext);

        OnClickListener oclBtnRepeat = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mediaPlayer.isLooping()){
                    mediaPlayer.setLooping(true);
                    repeat.setImageResource(R.drawable.repeat1);
                }
                else{
                    mediaPlayer.setLooping(false);
                    repeat.setImageResource(R.drawable.repeat);
                }
            }
        };

        repeat.setOnClickListener(oclBtnRepeat);



        OnClickListener oclBtnPrev = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos <= 0) {
                    pos = musicList.size() - 1;
                } else {
                    pos--;
                }
                initializeMusicPlayer(pos);
            }
        };

        prev.setOnClickListener(oclBtnPrev);


        OnClickListener oclBtnNext10 = new OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            }
        };

        next10.setOnClickListener(oclBtnNext10);

        OnClickListener oclBtnPrev10 = new OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            }
        };

        prev10.setOnClickListener(oclBtnPrev10);

        OnClickListener oclBtnRandom = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 0){
                    check = 1;
                    random.setImageResource(R.drawable.random1);
                }
                else{
                    check = 0;
                    random.setImageResource(R.drawable.random);
                }

            }
        };

        random.setOnClickListener(oclBtnRandom);





        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(check == 0){
                    if (pos < musicList.size() - 1) {
                        pos++;
                    } else {
                        pos = 0;
                    }
                }
                else{
                    pos = (int) (Math.random() * musicList.size());
                }
                initializeMusicPlayer(pos);
            }
        });

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };


    private void initializeMusicPlayer(int position) {


        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }

        String name = musicList.get(position).getName();
        songName.setText(name);


        Uri uri = Uri.parse(musicList.get(position).toString());


        mediaPlayer = MediaPlayer.create(this, uri);

        if(mediaPlayer.isPlaying() && mediaPlayer != null){
            mediaPlayer.pause();
            play.setImageResource(R.drawable.play);
        }
        else{
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
        }


    }

    private void updateSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        seekBar.setProgress(mediaPlayer.getCurrentPosition()/ 1000);
        //txtCurrentTime.setText(milliSecondsToTimer(player.getCurrentPosition()));
        //seekHandler.postDelayed(runnable, 50);
        handler.postDelayed(runnable, 50);
        timer.setText(millisecondsToString(mediaPlayer.getCurrentPosition()));
        maxTime.setText(millisecondsToString(mediaPlayer.getDuration()));
    }


    private String millisecondsToString(int milliseconds)  {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                    if (pos <= 0) {
                        pos = musicList.size() - 1;
                    } else {
                        pos--;
                    }
                    initializeMusicPlayer(pos);
                }
                else if(x1 > x2){
                    if(check == 0){
                        if (pos < musicList.size() - 1) {
                            pos++;
                        } else {
                            pos = 0;
                        }
                    }
                    else{
                        pos = (int) (Math.random() * musicList.size());
                    }
                    initializeMusicPlayer(pos);
                }
                break;

        }

        return false;
    }

}

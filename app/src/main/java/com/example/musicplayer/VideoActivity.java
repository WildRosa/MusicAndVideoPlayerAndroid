package com.example.musicplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    Bundle filmExtraData;
    VideoView videoPlayer;
    ArrayList<File> filmList;
    ImageView play, next, prev, start, repeat;
    private Uri uri;
    ActionBar actionBar;
    boolean checkRepeat = false, check = false, checkEnd = false;
    private int mCurrentPosition = -1;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        videoPlayer = findViewById(R.id.videoPlayer);
        //button = findViewById(R.id.butn);

        Intent intent = getIntent();
        filmExtraData = intent.getExtras();

        filmList = (ArrayList) filmExtraData.getParcelableArrayList("film");
        pos = filmExtraData.getInt("position", 0);

        //String name = filmList.get(pos).getName();
        //songName.setText(name);

        uri = Uri.parse(filmList.get(pos).toString());
        //mURI = getIntent().getData();
        if (savedInstanceState != null) {
            uri = Uri.parse(savedInstanceState.getString("URL"));
            mCurrentPosition = savedInstanceState.getInt("CURRENT");
            pos = savedInstanceState.getInt("Pos");

        }
        videoPlayer.setVideoURI(uri);
        videoPlayer.seekTo(mCurrentPosition);
        videoPlayer.start();
        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoPlayer);
        play =  findViewById(R.id.videoPlay);
        repeat = findViewById(R.id.videoRepeat);
        prev = findViewById(R.id.videoPrew);
        next = findViewById(R.id.videoNext);
        start = findViewById(R.id.videoStart);

        //videoPlayer.setOnPreparedListener(PreparedListener);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {actionBar = getSupportActionBar();
            actionBar.hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        OnClickListener oclBtnPlay = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPlayer.isPlaying() && videoPlayer != null) {
                    videoPlayer.pause();
                    play.setImageResource(R.drawable.videoplay);
                    //play.setImageResource(R.drawable.play);


                } else {
                    videoPlayer.start();
                    //play.setImageResource(R.drawable.pause);
                    play.setImageResource(R.drawable.videostop);

                }
            }
        };
        play.setOnClickListener(oclBtnPlay);

      OnClickListener oclBtnRepeat = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRepeat == false){
                    checkRepeat = true;

                    repeat.setImageResource(R.drawable.videorepeat1);
                }
                else{
                    checkRepeat = false;
                    repeat.setImageResource(R.drawable.videorepeat);
                }

            }
        };
        repeat.setOnClickListener(oclBtnRepeat);

        OnClickListener oclBtnStart = new OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.resume();


            }
        };
        start.setOnClickListener(oclBtnStart);

    videoPlayer.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(checkRepeat == true){
            videoPlayer.start();
            }

        }
    });

        OnClickListener oclBtnPrev = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos <= 0) {
                    pos = filmList.size() - 1;
                } else {
                    pos--;
                }
                initializeVideoPlayer(pos);
            }
        };

        prev.setOnClickListener(oclBtnPrev);

        OnClickListener oclBtnNext = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < filmList.size() - 1) {
                        pos++;
                    } else {
                        pos = 0;
                    }

                initializeVideoPlayer(pos);
            }
        };

        next.setOnClickListener(oclBtnNext);

videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(checkRepeat == false) {
            if (pos < filmList.size() - 1) {
                pos++;
            } else {
                pos = 0;
            }

            checkEnd = true;
            initializeVideoPlayer(pos);
        }
        else{
            videoPlayer.start();
        }
    }
});

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CURRENT", videoPlayer.getCurrentPosition());
        outState.putInt("Pos", pos);
        outState.putString("URL", uri.toString());
        super.onSaveInstanceState(outState);
    }

    private void initializeVideoPlayer(int position) {



        if(videoPlayer.isPlaying()){
            check = true;
        }
        else{
            check = false;
        }

        uri = Uri.parse(filmList.get(position).toString());
        videoPlayer.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoPlayer);

        if(check == false && checkEnd == false){

            play.setImageResource(R.drawable.videoplay);
            videoPlayer.pause();
        }
        else if(checkEnd == true){
            videoPlayer.start();
        }

        else{
            //videoPlayer.start();
            play.setImageResource(R.drawable.videostop);
        }
        checkEnd = false;

    }




}

package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    ListView mediaList;
    ArrayAdapter<String> mediaArrayAdapter;
    String[] song;
    ArrayList<File> musics;
    ArrayList<File> filims;
    ArrayList<File> songs;
    int posSong, posFilm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaList = findViewById(R.id.listView);

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                musics = findMediaFiles(Environment.getExternalStorageDirectory());
                filims = findFilmFiles(Environment.getExternalStorageDirectory());
                songs  = findMusicFiles(Environment.getExternalStorageDirectory());

                song = new String[musics.size()];
                for (int i = 0; i < musics.size(); i++){
                    song[i] = musics.get(i).getName().toString();
                }
                mediaArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, song);
                mediaList.setAdapter(mediaArrayAdapter);

                mediaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(musics.get(position).getName().toString().endsWith(".mp3")){
                            for(int i = 0; i < songs.size(); i++){

                                    if(musics.get(position).getName().toString().equals(songs.get(i).getName().toString())){
                                        posSong = i;
                                        break;
                                    }
                            }
                            startActivity(new Intent(MainActivity.this, PlayerActivity.class)
                                    .putExtra("song", songs)
                                    .putExtra("position", posSong));
                        }
                        else if(!musics.get(position).getName().toString().endsWith(".mp3")){
                            for(int i = 0; i < filims.size(); i++){
                                if(musics.get(position).getName().toString().equals(filims.get(i).getName().toString())){
                                    posFilm = i;
                                    break;
                                }
                            }
                            startActivity(new Intent(MainActivity.this, VideoActivity.class)
                                    .putExtra("film", filims)
                                    .putExtra("position", posFilm));
                        }



                    }
                });


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();

            }
        }).check();


    }


    private ArrayList <File> findMediaFiles(File file){
        ArrayList<File> mediafileobject = new ArrayList<>();

        File [] files = file.listFiles();

        for (File currentFiles: files) {

            if (currentFiles.isDirectory() && !currentFiles.isHidden()) {
                mediafileobject.addAll(findMediaFiles(currentFiles));
            } else {
                if (currentFiles.getName().endsWith(".mp3") || currentFiles.getName().endsWith(".mp4") || currentFiles.getName().endsWith(".wav") || currentFiles.getName().endsWith(".3gp") || currentFiles.getName().endsWith(".mkv")) {
                    mediafileobject.add(currentFiles);

                }
            }


        }
       return mediafileobject;
    }

    private ArrayList <File> findMusicFiles(File file) {
        ArrayList<File> musicFileObject = new ArrayList<>();
        File[] files = file.listFiles();

        for (File currentFiles : files) {

            if (currentFiles.isDirectory() && !currentFiles.isHidden()) {
                musicFileObject.addAll(findMusicFiles(currentFiles));
            } else {
                    if (currentFiles.getName().endsWith(".mp3")) {
                        musicFileObject.add(currentFiles);

                }
            }

        }
    return musicFileObject;
    }

    private ArrayList <File> findFilmFiles(File file){
        ArrayList<File> filmFileObject = new ArrayList<>();
        File[] files = file.listFiles();

        for (File currentFiles : files) {

            if (currentFiles.isDirectory() && !currentFiles.isHidden()) {
                filmFileObject.addAll(findFilmFiles(currentFiles));
            } else {
                if (currentFiles.getName().endsWith(".mp4") || currentFiles.getName().endsWith(".wav") || currentFiles.getName().endsWith(".3gp") || currentFiles.getName().endsWith(".mkv")) {
                    filmFileObject.add(currentFiles);

                }
            }

        }
        return filmFileObject;

    }

}
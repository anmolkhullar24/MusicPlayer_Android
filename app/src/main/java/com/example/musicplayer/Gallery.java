package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    ListView listviewforsongs;
    TextView  txt;
    String[] items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        txt = findViewById(R.id.tit);
        runtimePermissions();


    }

    public void runtimePermissions(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong (File file){
        ArrayList <File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile : files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findSong(singleFile));
            }
            else
                {
                    if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                        arrayList.add(singleFile);
                    }
            }

        }
        return arrayList;
    }

    public void displaySongs() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for(int i = 0; i <mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items );
        listviewforsongs = findViewById(R.id.songs);
        listviewforsongs.setAdapter(adapter);
        listviewforsongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = listviewforsongs.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),player.class)
                        .putExtra("songs",mySongs).putExtra("songName",songName)
                        .putExtra("pos",i));


            }
        });
    }

}

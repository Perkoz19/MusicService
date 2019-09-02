package com.example.musicservice;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String path = Environment.getExternalStorageDirectory().toString()+"/Music/";
    File directory;
    File[] files;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        directory = new File(path);
        files = directory.listFiles();
    }



    public void startPlayerService(View view){
        Intent i = new Intent(this, MusicService.class);
        //i.setAction("start");
        if(isMyServiceRunning(MusicService.class)){
            i.setAction("pause");
        }else {
            i.putExtra("songs", files);
            i.setAction("start");
            startService(i);
        }
    }

    public void nextPlayerService(View view){
        if(isMyServiceRunning(MusicService.class)) {
            Intent i = new Intent(this, MusicService.class);
            i.setAction("next");
            startService(i);
        }
    }

    public void previousPlayerService(View view){
        if(isMyServiceRunning(MusicService.class)) {
            Intent i = new Intent(this, MusicService.class);
            i.setAction("previous");
            startService(i);
        }
    }

    public void pausePlayerService(View view){
        if(isMyServiceRunning(MusicService.class)) {
            Intent i = new Intent(this, MusicService.class);
            i.setAction("pause");
            startService(i);
        }
    }

    public void stopPlayerService(View view){
        stopService(new Intent(this, MusicService.class));
    }

    public void toggleRandomPlayerService(View view){
        if(isMyServiceRunning(MusicService.class)) {
            Intent i = new Intent(this, MusicService.class);
            i.setAction("randd");
            startService(i);
        }
    }
}

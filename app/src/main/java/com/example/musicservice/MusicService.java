package com.example.musicservice;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicService extends Service {

    public MediaPlayer player;
    private int current = 0;
    private int randcurr = 0;
    private File[] files = null;
    private Uri uri;
    private int listlength;
    private boolean mpreleased = true;
    private boolean random = false;
    private File[] songsToPlay = null;
    private File[] songsPlayed = null;
    private List<Integer> randoms = new ArrayList<>();


    private void setSource(Intent intent){
        files = (File[]) intent.getSerializableExtra("songs");
        uri = Uri.fromFile(files[current]);
        listlength = files.length;
        for(int i = 0; i < listlength; i++){
            randoms.add(i);
        }
        Collections.shuffle(randoms);
    }

    private void toggleRandom(){
        if(random){
            random = false;
        }else{
            random = true;
        }
    }

    private void togglePlay(){
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }
    }

    private void playNext(){
        if(!mpreleased) {
            mpreleased = true;
            player.reset();
            player.release();
            if (current < listlength - 1) {
                current++;
            } else {
                current = 0;
            }
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(files[current].getAbsolutePath());
            player.prepare();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "error!!!!", Toast.LENGTH_LONG).show();
        }
        player.setLooping(false);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        mpreleased = false;
        player.start();
    }

    private void playPrev(){
        if(!mpreleased) {
            mpreleased = true;
            player.reset();
            player.release();
            if (current > 0) {
                current--;
            } else {
                current = listlength-1;
            }
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(files[current].getAbsolutePath());
            player.prepare();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "error!!!!", Toast.LENGTH_LONG).show();
        }
        player.setLooping(false);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        mpreleased = false;
        player.start();
    }

    private void playPrevRand(){
        if(!mpreleased) {
            mpreleased = true;
            player.reset();
            player.release();
            if (randcurr > 0) {
                randcurr--;
            } else {
                randcurr = listlength-1;
            }
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(files[randoms.get(randcurr)].getAbsolutePath());
            player.prepare();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "error!!!!", Toast.LENGTH_LONG).show();
        }
        player.setLooping(false);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        mpreleased = false;
        player.start();
    }

    private void playNextRand(){
        if(!mpreleased) {
            mpreleased = true;
            player.reset();
            player.release();
            if (randcurr < listlength - 1) {
                randcurr++;
            } else {
                randcurr = 0;
            }
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(files[randoms.get(randcurr)].getAbsolutePath());
            player.prepare();
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "error!!!!", Toast.LENGTH_LONG).show();
        }
        player.setLooping(false);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
        mpreleased = false;
        player.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate(){
        Toast.makeText(this, "service started", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){

        String action = intent.getAction();
        switch(action){
            case "start":
                setSource(intent);
                //songsToPlay = files;
                playNext();
                break;
            case "pause":
                togglePlay();
                break;
            case "next":
                if(random){
                    playNextRand();
                }else {
                    playNext();
                }
                break;
            case "previous":
                if(random) {
                    playPrevRand();
                }else{
                    playPrev();
                }
                break;
            case "randd":
                toggleRandom();
                break;
        }

        Toast.makeText(this, "player started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mpreleased = true;
        if(player != null) {
            player.stop();
            player.reset();
            player.release();
        }
        Toast.makeText(this, "service stopped", Toast.LENGTH_LONG).show();
    }
}

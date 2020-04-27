package com.job4j.simpleplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer media;
    private int[] resourceIDs;
    private int count;
    private Button play;
    private Button stop;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        uri = getIntent().getData();
        count = 0;
        Field[] fields = R.raw.class.getFields();
        resourceIDs = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            try {
                resourceIDs[i] = fields[i].getInt(fields[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (uri == null) {
            media = MediaPlayer.create(this, resourceIDs[count]);
        } else {
            media = new MediaPlayer();
            try {
                media.setDataSource(MainActivity.this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                media.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        play.setOnClickListener(this::play);
        stop.setOnClickListener(this::stop);
        media.setOnCompletionListener(this::mediaCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        media.release();
        media = null;
    }

    private void mediaCallback(MediaPlayer mediaPlayer) {
        count++;
        if (count == resourceIDs.length) {
            count = 0;
            return;
        }
        media.reset();
        media = MediaPlayer.create(this, resourceIDs[count]);
        media.start();
        media.setOnCompletionListener(this::mediaCallback);
    }

    private void play(View view) {
        try {
            media.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop(View view) {
        try {
            media.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

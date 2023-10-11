package com.example.servicesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class notification_layout extends AppCompatActivity {
    private MediaPlayer player;
    private Button btnStartStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_layout);

        btnStartStop = findViewById(R.id.btnStartStop);
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    stopMusic();
                } else {
                    player.start();
                    btnStartStop.setText("Stop");
                }
            }
        });
    }

    private void stopMusic() {
        player.pause();
        btnStartStop.setText("Start");

        // Stop the music service
        Intent stopIntent = new Intent(this, servicesClass.class);
        stopService(stopIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}
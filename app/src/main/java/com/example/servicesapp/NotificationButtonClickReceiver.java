package com.example.servicesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.RemoteViews;

public class NotificationButtonClickReceiver extends BroadcastReceiver {


    private MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals("play_pause")) {
                    // Stop the music if it's playing
                    if (player != null && player.isPlaying()) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                    // You can also stop the service here if needed
                    Intent stopServiceIntent = new Intent(context, servicesClass.class);
                    context.stopService(stopServiceIntent);
                }
            }
        }
    }
}
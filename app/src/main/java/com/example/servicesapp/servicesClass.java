package com.example.servicesapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.security.Provider;

public class servicesClass extends Service {
    private MediaPlayer player;
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());

        // Acquire a WakeLock to prevent the CPU from sleeping
        WakeLockHelper.acquireWakeLock(this);

        if (player == null) {
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            player.setLooping(true);
            player.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        // Release the WakeLock
        WakeLockHelper.releaseWakeLock();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification_layout);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Intent playPauseIntent = new Intent(this, NotificationButtonClickReceiver.class);
        playPauseIntent.setAction("play_pause");
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnStartStop, playPausePendingIntent);

        return new NotificationCompat.Builder(this, "ChannelId")
                .setContentTitle("Music Service")
                .setContentText("Playing music in the background")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setTicker("Music Service")
                .build();
    }
}
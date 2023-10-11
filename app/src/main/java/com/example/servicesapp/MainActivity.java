package com.example.servicesapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnstart, btnstop,btnBubblee;
    private boolean isServiceRunning = false;
    private boolean isOverlayPermissionGranted = false;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnstart = findViewById(R.id.btnStart);
        btnstop = findViewById(R.id.btnStop);
        btnBubblee = findViewById(R.id.btnBubble);


        startService(new Intent(MainActivity.this,FloatingViewService.class));

//        btnBubblee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isOverlayPermissionGranted) {
//                    requestOverlayPermission();
//                } else {
//                    // Start the BubbleService
//                    Intent bubbleIntent = new Intent(MainActivity.this, BubbleService.class);
//                    startService(bubbleIntent);
//                }
//            }
//        });

// Check if overlay permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            btnBubblee.setText("Request Overlay Permission");
        } else {
            isOverlayPermissionGranted = true;
            btnBubblee.setText("Start Bubble Service");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ChannelId",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            Toast.makeText(this, "Your Mobile version is old", Toast.LENGTH_SHORT).show();
        }

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isServiceRunning) {
                    // Check and request battery optimization exemptions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!isIgnoringBatteryOptimizations()) {
                            requestBatteryOptimizationsExemption();
                        }
                    }

                    Intent serviceIntent = new Intent(MainActivity.this, servicesClass.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    }
                    isServiceRunning = true;
                }
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServiceRunning) {
                    Intent stopIntent = new Intent(MainActivity.this, servicesClass.class);
                    stopService(stopIntent);
                    isServiceRunning = false;
                }
            }
        });
    }


    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if overlay permission is not granted
            if (!Settings.canDrawOverlays(this)) {
                // Open the permission request screen
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Check if battery optimization is already exempted for your app
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        return pm.isIgnoringBatteryOptimizations(packageName);
    }

    // Request battery optimization exemptions for your app
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestBatteryOptimizationsExemption() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
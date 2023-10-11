package com.example.servicesapp;

import android.content.Context;
import android.os.PowerManager;

public class WakeLockHelper {
    private static PowerManager.WakeLock wakeLock;

    public static void acquireWakeLock(Context context) {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag"
            );
            wakeLock.acquire();
        }
    }

    public static void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
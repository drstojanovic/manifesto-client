package com.example.stefan.manifesto.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.stefan.manifesto.ManifestoApplication;

public class HelperUtils {

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ManifestoApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

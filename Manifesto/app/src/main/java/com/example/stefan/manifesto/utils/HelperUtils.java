package com.example.stefan.manifesto.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.example.stefan.manifesto.ManifestoApplication;

import java.util.List;

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

    public static boolean isInList(List<Integer> list, Integer item) {
        for (Integer integer : list) {
            if(integer.equals(item)) {
                return true;
            }
        }
        return false;
    }

}

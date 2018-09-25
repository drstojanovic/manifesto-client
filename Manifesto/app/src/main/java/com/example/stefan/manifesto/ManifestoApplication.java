package com.example.stefan.manifesto;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

public class ManifestoApplication extends Application{

    private static ManifestoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        application = this;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }
}

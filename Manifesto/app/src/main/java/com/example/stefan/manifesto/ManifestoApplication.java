package com.example.stefan.manifesto;

import android.app.Application;
import android.content.Context;

public class ManifestoApplication extends Application{

    private static ManifestoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }
}

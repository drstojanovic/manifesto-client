package com.example.stefan.manifesto.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stefan.manifesto.ManifestoApplication;

public class SharedPreferencesUtil {

    private static final String PREFS_NAME = "app_preferences";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = ManifestoApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = getSharedPreferences().edit();
        }
        return editor;
    }

    public static void write(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static String read(String key) {
        return getSharedPreferences().getString(key, "error reading string from shared preferences");
    }

}

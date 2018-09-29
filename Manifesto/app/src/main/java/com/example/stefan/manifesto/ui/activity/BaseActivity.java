package com.example.stefan.manifesto.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.stefan.manifesto.R;

public abstract class BaseActivity extends AppCompatActivity {

    public void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(title);
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void navigateToActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void navigateToActivity(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    public void navigateToActivityForResult(Class activityClass, int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }

    public void navigateToActivityForResult(Class activityClass, int requestCode, Bundle data) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(data);
        startActivityForResult(intent, requestCode);
    }


}

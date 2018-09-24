package com.example.stefan.manifesto.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

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

}

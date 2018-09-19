package com.example.stefan.manifesto.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void makeToast(@StringRes Integer message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void navigateToActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(getContext(), activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void navigateToActivity(Class activityClass) {
        startActivity(new Intent(getContext(), activityClass));
    }

}

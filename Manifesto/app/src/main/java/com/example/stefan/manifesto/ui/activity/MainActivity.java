package com.example.stefan.manifesto.ui.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.ui.fragment.EventListFragment;
import com.example.stefan.manifesto.ui.fragment.FeedFragment;
import com.example.stefan.manifesto.utils.SharedPreferencesUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, EventListFragment.newInstance(), EventListFragment.class.getSimpleName()).commit();

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }



}

package com.example.stefan.manifesto.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.ui.fragment.EventListFragment;
import com.example.stefan.manifesto.ui.fragment.FeedFragment;
import com.example.stefan.manifesto.utils.UserSession;
import com.example.stefan.manifesto.viewmodel.UserProfileViewModel;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (savedInstanceState == null) {
            putFragment(FeedFragment.newInstance(), false, FeedFragment.class.getSimpleName());
        }
    }

    private void initViews() {
        initNavMenu();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavMenu() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        View hearderview = navigationView.getHeaderView(0);
        LinearLayout container = hearderview.findViewById(R.id.nav_header);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.EXTRA_USER_ID, UserSession.getUser().getId());
                startActivity(intent);

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        Glide.with(this)
                .load(UserSession.getUser().getImage())
                .into(((ImageView) hearderview.findViewById(R.id.nav_header_image)));
        ((TextView) hearderview.findViewById(R.id.nav_header_user_name)).setText(UserSession.getUser().getName());
        ((TextView) hearderview.findViewById(R.id.nav_header_user_city)).setText(UserSession.getUser().getCity());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (isNavItemChecked(item)) return false;

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_item_events:
                putFragment(EventListFragment.newInstance(), true, EventListFragment.class.getSimpleName());
                break;
            case R.id.menu_item_feed:
                putFragment(FeedFragment.newInstance(), true, FeedFragment.class.getSimpleName());
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNavItemChecked(MenuItem item) {
        if (item.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void putFragment(Fragment fragment, boolean addToBackStack, String tag) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().addToBackStack(tag).replace(R.id.fragment_container, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}

package com.example.stefan.manifesto.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.service.NotificationService;
import com.example.stefan.manifesto.ui.fragment.EventListFragment;
import com.example.stefan.manifesto.ui.fragment.FeedFragment;
import com.example.stefan.manifesto.ui.fragment.PeopleFragment;
import com.example.stefan.manifesto.utils.UserSession;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FRESH_START = "FRESH_START";
    public static final int RC_NEW_POST_NOTIFICATION = 1;
    public static final String ACTION_RESET_NAV_HEADER_DATA = "ACTION_RESET_NAV_HEADER_DATA";
    private DrawerLayout drawerLayout;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (savedInstanceState == null) {
            replaceFragment(FeedFragment.newInstance(), false, FeedFragment.class.getSimpleName());
        }
        startService(new Intent(this, NotificationService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(resetHeaderDataBroadcast, new IntentFilter(ACTION_RESET_NAV_HEADER_DATA));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // if (HelperUtils.isMyServiceRunning(NotificationService.class)) {
//            makeToast("Stoping notification service.");
//            stopService(new Intent(this, NotificationService.class));
//        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(resetHeaderDataBroadcast);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(FRESH_START, false)) {
            replaceFragment(FeedFragment.newInstance(), false, FeedFragment.class.getSimpleName());
        }
    }

    private void initViews() {
        initNavMenu();

        initToolbar("Manifesto");

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavMenu() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        headerView = navigationView.getHeaderView(0);
        LinearLayout container = headerView.findViewById(R.id.nav_header);
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

        setHeaderData();
    }

    private void setHeaderData() {
        Glide.with(this)
                .load(UserSession.getUser().getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(((ImageView) headerView.findViewById(R.id.nav_header_image)));
        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(UserSession.getUser().getName());
        ((TextView) headerView.findViewById(R.id.nav_header_user_city)).setText(UserSession.getUser().getCity());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_item_events:
                replaceFragment(EventListFragment.newInstance(), true, EventListFragment.class.getSimpleName());
                break;
            case R.id.menu_item_feed:
                replaceFragment(FeedFragment.newInstance(), true, FeedFragment.class.getSimpleName());
                break;
            case R.id.menu_item_people:
                replaceFragment(PeopleFragment.newInstance(), true, PeopleFragment.class.getSimpleName());
                break;
            case R.id.menu_item_settings:
                navigateToActivity(SettingsActivity.class);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String tag) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().addToBackStack(tag).replace(R.id.fragment_container, fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }



    private BroadcastReceiver resetHeaderDataBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setHeaderData();
        }
    };

}

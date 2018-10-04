package com.example.stefan.manifesto.ui.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivitySettingsBinding;
import com.example.stefan.manifesto.model.NotificationsSettingsItem;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.service.LocationTrackingService;
import com.example.stefan.manifesto.service.NotificationService;
import com.example.stefan.manifesto.ui.adapter.NotificationSettingsAdapter;
import com.example.stefan.manifesto.utils.HelperUtils;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity implements NotificationSettingsAdapter.OnSettingSelectedListener {

    private static final int RC_SELECT_PROFILE_IMAGE = 1;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;
    private NotificationSettingsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);

        initToolbar("Settings");
        initViews();
        setUpObservers();
    }

    private void initViews() {
        if (HelperUtils.isMyServiceRunning(LocationTrackingService.class)) {
            binding.switchTracking.setChecked(true);
        }

        binding.recyclerNotificationSettings.setHasFixedSize(true);
        binding.recyclerNotificationSettings.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNotificationSettings.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_settings:
                viewModel.saveChanges();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.imageLoading.setVisibility(View.VISIBLE);
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpObservers() {
        viewModel.getImageClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                startActivityForResult(new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT), SettingsActivity.RC_SELECT_PROFILE_IMAGE);
            }
        });

        viewModel.getSavingResponse().observe(this, new Observer<ResponseMessage<User>>() {
            @Override
            public void onChanged(@Nullable ResponseMessage<User> userResponseMessage) {
                if (userResponseMessage != null && userResponseMessage.getMessage() != null) {
                    makeToast(userResponseMessage.getMessage());
                }
                binding.progressBar.setVisibility(View.GONE);
                binding.imageLoading.setVisibility(View.GONE);
                LocalBroadcastManager.getInstance(SettingsActivity.this).sendBroadcast(new Intent(MainActivity.ACTION_RESET_NAV_HEADER_DATA));
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRESH_START, true);
                startActivity(intent);
            }
        });

        viewModel.getSwitchTrackingService().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean switchOn) {
                if (switchOn == null) return;

                if (switchOn) {
                    checkPermissionAndStartLocationTrackingService();
                } else {
                    stopService(new Intent(SettingsActivity.this, LocationTrackingService.class));
                }
            }
        });

        viewModel.getSettingsItems().observe(this, new Observer<ArrayList<NotificationsSettingsItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<NotificationsSettingsItem> notificationsSettingsItems) {
                setAdapter(notificationsSettingsItems);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingsActivity.RC_SELECT_PROFILE_IMAGE) {
            viewModel.setSelectedImage(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationTrackingService();
                }
            }
        }
    }

    private void checkPermissionAndStartLocationTrackingService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            startLocationTrackingService();
        }
    }

    private void startLocationTrackingService() {
        startService(new Intent(this, LocationTrackingService.class));
    }

    public void setAdapter(List<NotificationsSettingsItem> items) {
        if (adapter == null) {
            adapter = new NotificationSettingsAdapter(items, this);
            binding.recyclerNotificationSettings.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCheckBoxSelected(NotificationsSettingsItem item) {
        viewModel.checkedSettingItem(item);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NotificationService.ACTION_RESTART));
    }
}

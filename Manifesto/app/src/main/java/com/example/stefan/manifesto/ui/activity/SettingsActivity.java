package com.example.stefan.manifesto.ui.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivitySettingsBinding;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.service.LocationTrackingService;
import com.example.stefan.manifesto.utils.HelperUtils;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.viewmodel.AddPostViewModel;
import com.example.stefan.manifesto.viewmodel.SettingsViewModel;

public class SettingsActivity extends BaseActivity {

    private static final int RC_SELECT_PROFILE_IMAGE = 1;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

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
}

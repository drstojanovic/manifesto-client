package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivitySettingsBinding;
import com.example.stefan.manifesto.viewmodel.AddPostViewModel;
import com.example.stefan.manifesto.viewmodel.SettingsViewModel;

public class SettingsActivity extends BaseActivity {

    private static final int RC_SELECT_PROFILE_IMAGE = 1;
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);

//        setUpViews();
        initToolbar("Settings");
        setUpObservers();
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
                viewModel.saveSettings();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingsActivity.RC_SELECT_PROFILE_IMAGE) {
            viewModel.saveSelectedImage(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityShowEventBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.service.NotificationService;
import com.example.stefan.manifesto.ui.fragment.EventListFragment;
import com.example.stefan.manifesto.viewmodel.EventProfileViewModel;
import com.example.stefan.manifesto.viewmodel.EventProfileViewModelFactory;

public class ShowEventActivity extends BaseActivity {

    private ActivityShowEventBinding binding;
    private EventProfileViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_show_event);
            viewModel = ViewModelProviders.of(this,
                    new EventProfileViewModelFactory(getIntent().getIntExtra(EventListFragment.EXTRA_EVENT_ID, 0)))
                    .get(EventProfileViewModel.class);

            initToolbar("Event");
            setUpObservers();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setUpObservers() {
        viewModel.getEvent().observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
               binding.setViewModel(viewModel);
            }
        });

        viewModel.getResetQueues().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                LocalBroadcastManager.getInstance(ShowEventActivity.this).sendBroadcast(new Intent(NotificationService.ACTION_RESTART));
            }
        });
    }

}

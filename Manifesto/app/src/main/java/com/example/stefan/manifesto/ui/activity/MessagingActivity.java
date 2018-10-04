package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;

import com.example.stefan.manifesto.ManifestoApplication;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityMessagingBinding;
import com.example.stefan.manifesto.model.Message;
import com.example.stefan.manifesto.ui.adapter.MessageAdapter;
import com.example.stefan.manifesto.viewmodel.MessagingViewModel;
import com.example.stefan.manifesto.viewmodel.MessagingViewModelFactory;

import java.util.List;

public class MessagingActivity extends BaseActivity {

    public static final String ACTION_NEW_MESSAGE = "ACTION_NEW_MESSAGE";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final int RC_NEW_MESSAGE_NOTIFICATION = 1;
    private MessagingViewModel viewModel;
    private ActivityMessagingBinding binding;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null || getIntent().getIntExtra(EXTRA_USER_ID, -1) == -1) {
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_messaging);
        viewModel = ViewModelProviders.of(this,
                new MessagingViewModelFactory(getIntent().getIntExtra(EXTRA_USER_ID, -1))).get(MessagingViewModel.class);
        binding.setViewModel(viewModel);

        initViews();
        setUpObservers();
    }

    private void initViews() {
        binding.recyclerMessages.setHasFixedSize(true);
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpObservers() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                setAdapter(messages);
            }
        });

        viewModel.getSendButtonClick().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(@Nullable Message message) {
                adapter.addMessage(message);
                binding.recyclerMessages.scrollToPosition(adapter.getItemCount()-1);
            }
        });
    }

    private void setAdapter(List<Message> messages) {
        if (adapter == null) {
            adapter = new MessageAdapter(messages);
            binding.recyclerMessages.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.recyclerMessages.scrollToPosition(adapter.getItemCount() - 1);
        LocalBroadcastManager.getInstance(this).registerReceiver(newMessageReceiver, new IntentFilter(ACTION_NEW_MESSAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ManifestoApplication.messagingActivityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ManifestoApplication.messagingActivityPaused();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newMessageReceiver);
    }

    private BroadcastReceiver newMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message = intent.getParcelableExtra(EXTRA_MESSAGE);
            adapter.addMessage(message);
            binding.recyclerMessages.scrollToPosition(adapter.getItemCount()-1);
        }
    };
}

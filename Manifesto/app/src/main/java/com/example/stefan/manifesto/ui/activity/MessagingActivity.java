package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityMessagingBinding;
import com.example.stefan.manifesto.model.Message;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.ui.adapter.MessageAdapter;
import com.example.stefan.manifesto.viewmodel.MessagingViewModel;
import com.example.stefan.manifesto.viewmodel.MessagingViewModelFactory;

import java.util.List;

public class MessagingActivity extends BaseActivity {

    private MessagingViewModel viewModel;
    private ActivityMessagingBinding binding;
    private MessageAdapter adapter;
    public static final String EXTRA_USER_OBJECT = "EXTRA_USER_OBJECT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null || getIntent().getParcelableExtra(EXTRA_USER_OBJECT) == null) {
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_messaging);
        viewModel = ViewModelProviders.of(this,
                new MessagingViewModelFactory((User) getIntent().getParcelableExtra(EXTRA_USER_OBJECT))).get(MessagingViewModel.class);
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


}

package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityShowEventBinding;
import com.example.stefan.manifesto.model.Event;
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

            setUpObservers();
        }

    }

    private void setUpObservers() {
        viewModel.getEvent().observe(this, new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
               binding.setViewModel(viewModel);
            }
        });
    }

}

package com.example.stefan.manifesto.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.ui.activity.ShowEventActivity;
import com.example.stefan.manifesto.ui.adapter.EventAdapter;

public class EventListFragment extends BaseFragment implements EventAdapter.OnEventItemClickListener {

    public static EventListFragment newInstance() {

        Bundle args = new Bundle();

        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onEventItemClick(Integer eventId) {
        navigateToActivity(ShowEventActivity.class);
    }
}

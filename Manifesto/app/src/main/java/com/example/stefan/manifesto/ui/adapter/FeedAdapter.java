package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder> {
    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FeedItemViewHolder extends RecyclerView.ViewHolder {

        public FeedItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}

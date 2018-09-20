package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.databinding.FragmentFeedBinding;
import com.example.stefan.manifesto.databinding.ItemEventBinding;
import com.example.stefan.manifesto.databinding.ItemFeedBinding;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.ui.fragment.FeedFragment;
import com.example.stefan.manifesto.viewmodel.FeedItemViewModel;
import com.example.stefan.manifesto.viewmodel.FeedViewModel;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedItemViewHolder> {

    private OnPostClickInterface listener;
    private List<Post> posts;

    public FeedAdapter(List<Post> posts, OnPostClickInterface listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(ItemFeedBinding.inflate(LayoutInflater.from(parent.getContext()), parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, final int position) {
        final Post post = posts.get(position);
        holder.bind(new FeedItemViewModel(post));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPostClick(post.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }



    class FeedItemViewHolder extends RecyclerView.ViewHolder {
        private ItemFeedBinding binding;

        public FeedItemViewHolder(ItemFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeedItemViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public interface OnPostClickInterface {
        void onPostClick(int postId);
    }
}

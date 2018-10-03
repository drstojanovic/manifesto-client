package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.stefan.manifesto.databinding.ItemMessageReceiverBinding;
import com.example.stefan.manifesto.databinding.ItemMessageSenderBinding;
import com.example.stefan.manifesto.model.Message;
import com.example.stefan.manifesto.utils.UserSession;
import com.example.stefan.manifesto.viewmodel.MessageItemViewModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_SENDER = 1;
    private static final int ITEM_TYPE_RECEIVER = 2;
    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == ITEM_TYPE_SENDER ? new MessageSentItemViewHolder(ItemMessageSenderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false))
                : new MessageReceivedItemViewHolder(ItemMessageReceiverBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (getItemViewType(position) == ITEM_TYPE_SENDER) {
            ((MessageSentItemViewHolder) holder).binding.setViewModel(new MessageItemViewModel(message));
        } else {
            ((MessageReceivedItemViewHolder) holder).binding.setViewModel(new MessageItemViewModel(message));
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        int myId = UserSession.getUser().getId();
        Message message = messages.get(position);
        return message.getSenderId() == myId ? ITEM_TYPE_SENDER : ITEM_TYPE_RECEIVER;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        notifyItemInserted(getItemCount());
    }


    public class MessageSentItemViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageSenderBinding binding;

        public MessageSentItemViewHolder(ItemMessageSenderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageItemViewModel viewModel) {
            this.binding.setViewModel(viewModel);
        }
    }

    public class MessageReceivedItemViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageReceiverBinding binding;

        public MessageReceivedItemViewHolder(ItemMessageReceiverBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageItemViewModel viewModel) {
            this.binding.setViewModel(viewModel);
        }
    }
}

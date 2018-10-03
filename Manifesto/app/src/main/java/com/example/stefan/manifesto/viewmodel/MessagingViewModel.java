package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.Message;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.MessageRepository;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MessagingViewModel extends BaseViewModel {

    private MessageRepository repository = new MessageRepository();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private ObservableField<String> messageText = new ObservableField<>();
    private SingleLiveEvent<Message> sendButtonClick = new SingleLiveEvent<>();
    private User interlocutor;


    public MessagingViewModel(User user) {
        this.interlocutor = user;
        loadChat();
    }

    private void loadChat() {
        repository.getChat(interlocutor.getId(),
                new SingleObserver<List<Message>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Message> list) {
                        messages.setValue(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void addMessage(Message message) {
        repository.addMessage(message, new SingleObserver<Void>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void onSendClick() {
        Message message = new Message(0, messageText.get(), DateTime.now().toDate(), UserSession.getUser().getId(), interlocutor.getId());
        sendButtonClick.setValue(message);
        addMessage(message);
        messageText.set("");
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public ObservableField<String> getMessageText() {
        return messageText;
    }

    public void setMessageText(ObservableField<String> messageText) {
        this.messageText = messageText;
    }

    public LiveData<Message> getSendButtonClick() {
        return sendButtonClick;
    }
}

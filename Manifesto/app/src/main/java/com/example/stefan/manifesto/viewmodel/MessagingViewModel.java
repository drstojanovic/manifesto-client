package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.Message;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.MessageRepository;
import com.example.stefan.manifesto.repository.UserRepository;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MessagingViewModel extends BaseViewModel {

    private MessageRepository repository = new MessageRepository();
    private UserRepository userRepository = new UserRepository();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private ObservableField<String> messageText = new ObservableField<>();
    private SingleLiveEvent<Message> sendButtonClick = new SingleLiveEvent<>();
    private ObservableField<User> interlocutor = new ObservableField<>();


    public MessagingViewModel(int interlocutorId) {
        if (interlocutorId == -1) return;
        interlocutor.set(new User());
        getUser(interlocutorId);
        loadChat(interlocutorId);
    }

    private void getUser(int interlocutorId) {
        userRepository.getUser(interlocutorId,
                new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(User user) {
                        interlocutor.set(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void loadChat(int interlocutorId) {
        repository.getChat(interlocutorId,
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

    private void addMessage(final Message message) {
        repository.addMessage(message, new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean status) {
                sendButtonClick.setValue(message);
                messageText.set("");
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void onSendClick() {
        Message message = new Message(0, messageText.get(), DateTime.now().toDate(), UserSession.getUser().getId(), interlocutor.get().getId());
        addMessage(message);
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

    public ObservableField<User> getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(ObservableField<User> interlocutor) {
        this.interlocutor = interlocutor;
    }
}

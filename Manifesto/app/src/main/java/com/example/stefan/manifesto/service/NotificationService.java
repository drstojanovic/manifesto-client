package com.example.stefan.manifesto.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.stefan.manifesto.ManifestoApplication;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.model.PostNotificationMessage;
import com.example.stefan.manifesto.ui.activity.MainActivity;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NotificationService extends Service {

    private static final String MY_QUEUE = "user_queue_" + UserSession.getUser().getId();
    private static final String EXCHANGE_NAME = "post_notifications";
    private static final String EXCHANGE_TYPE = "topic";

    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private Thread subscriberThread;
    private Channel channel;

    private int POST_NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        basicSetup();
        startSubscriber();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        endSubscriber();
    }

    private void basicSetup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectionFactory.setHost(Constants.HOST);
            }
        }).start();
    }

    private void startSubscriber() {
        subscriberThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = connectionFactory.newConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
                    channel.queueDeclare(MY_QUEUE, true, false, false, null);
                    channel.queueBind(MY_QUEUE, EXCHANGE_NAME, "#");

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String message = new String(body, "UTF-8");
                            Gson gson = new Gson();
                            PostNotificationMessage notificationMessage = gson.fromJson(message, PostNotificationMessage.class);
                            if (notificationMessage != null) {
                                displayNotification(notificationMessage);
                            }
                        }
                    };
                    channel.basicConsume(MY_QUEUE, true, consumer);

                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
        subscriberThread.start();
    }

    private void endSubscriber() {
        try {
            if (channel != null) {
                channel.close();
            }
            subscriberThread.interrupt();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNotification(PostNotificationMessage notificationMessage) {

        Intent intent = new Intent (this, MainActivity.class);
        intent.putExtra("EXTRA_POST_ID", notificationMessage.getPostId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MainActivity.RC_NEW_POST_NOTIFICATION,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ManifestoApplication.getContext(), Constants.POST_NOTIFICATIONS_CHANNEL)
                .setContentTitle("New Post")
                .setContentText(notificationMessage.getMessage())
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(POST_NOTIFICATION_ID, builder.build());
    }

}

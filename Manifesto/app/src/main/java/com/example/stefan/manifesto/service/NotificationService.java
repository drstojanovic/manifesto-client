package com.example.stefan.manifesto.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.stefan.manifesto.ManifestoApplication;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.NotificationsSettingsItem;
import com.example.stefan.manifesto.model.PostNotificationMessage;
import com.example.stefan.manifesto.model.UserLocation;
import com.example.stefan.manifesto.repository.UserRepository;
import com.example.stefan.manifesto.ui.activity.MainActivity;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.SharedPrefsUtils;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.support.constraint.Constraints.TAG;

public class NotificationService extends Service {

    private static final String MY_QUEUE = "user_queue_" + UserSession.getUser().getId();
    private static final String EXCHANGE_NAME = "post_notifications";
    private static final String EXCHANGE_TYPE = "topic";
    public static final String ACTION_RESTART = "ACTION_RESTART";
    private static final float EMERGENCY_NEARBY_DISTANCE = 100;

    private ConnectionFactory connectionFactory = new ConnectionFactory();
    private Thread subscriberThread;
    private Channel channel;
    private Connection connection;
    boolean isEmergencyNearby;


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
        LocalBroadcastManager.getInstance(this).registerReceiver(switchBroadcast,
                new IntentFilter(ACTION_RESTART));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        endSubscriber();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(switchBroadcast);
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
                    connection = connectionFactory.newConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
                    channel.queueDeclare(MY_QUEUE, true, false, false, null);
//                    channel.queueBind(MY_QUEUE, EXCHANGE_NAME, "#");
                    generateBindingKeysAndBindQueues();

                    channel.addShutdownListener(new ShutdownListener() {
                        @Override
                        public void shutdownCompleted(ShutdownSignalException cause) {
                            Log.e(TAG, "shutdownCompleted: " + cause);
                        }
                    });

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String message = new String(body, "UTF-8");
                            Gson gson = new Gson();
                            PostNotificationMessage notificationMessage = gson.fromJson(message, PostNotificationMessage.class);
                            if (notificationMessage != null && notificationMessage.getUserId() != null
                                    && notificationMessage.getUserId() != UserSession.getUser().getId()) {

                                if(!testAndShowEmergencyNearbyNotification(notificationMessage)) {
                                    displayNotification(notificationMessage);
                                };
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
            connection.close();
            subscriberThread.interrupt();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    private void generateBindingKeysAndBindQueues() throws IOException {
        for (Event event : UserSession.getFollowedEvents()) {
            int settingsOption = SharedPrefsUtils.getInstance().getIntValue(Constants.NOTIF_SETTINGS_ + event.getId(), -1);
            if (settingsOption == -1) continue;
            NotificationsSettingsItem.Scope scope = NotificationsSettingsItem.Scope.values()[settingsOption];

            channel.queueUnbind(MY_QUEUE, EXCHANGE_NAME, event.getName() + ".emergency");
            channel.queueUnbind(MY_QUEUE, EXCHANGE_NAME, event.getName() + ".*");

            StringBuilder builder = new StringBuilder(event.getName());
            switch (scope) {
                case ALL:
                    builder.append(".*");
                    break;
                case EMERGENCY:
                case EMERGENCY_NEARBY:
                    builder.append(".emergency");
                    break;
                case NONE:
                    continue;
            }
            channel.queueBind(MY_QUEUE, EXCHANGE_NAME, builder.toString());
        }
    }

    private boolean testAndShowEmergencyNearbyNotification(final PostNotificationMessage notificationMessage) {
        int settingsOption = SharedPrefsUtils.getInstance().getIntValue(Constants.NOTIF_SETTINGS_ + notificationMessage.getEventId(), -1);
        NotificationsSettingsItem.Scope scope = NotificationsSettingsItem.Scope.values()[settingsOption];
        if (scope != NotificationsSettingsItem.Scope.EMERGENCY_NEARBY) return false;

        UserRepository userRepository = new UserRepository();
        userRepository.getUserLocation(new SingleObserver<UserLocation>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(UserLocation userLocation) {
                float[] distances = new float[10];
                Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                        notificationMessage.getPostLatitude(), notificationMessage.getPostLongitude(), distances);
                if (distances[0] <= EMERGENCY_NEARBY_DISTANCE) {
                    displayEmergencyNearbyNotification(notificationMessage);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        return true;
    }

    private void displayNotification(PostNotificationMessage notificationMessage) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXTRA_POST_ID", notificationMessage.getPostId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MainActivity.RC_NEW_POST_NOTIFICATION,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ManifestoApplication.getContext(), Constants.POST_NOTIFICATIONS_CHANNEL)
                .setContentTitle("New Post")
                .setContentText(notificationMessage.getMessage())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_feed)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{500, 400, 200, 200, 200, 200, 200, 200, 200})
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(POST_NOTIFICATION_ID, builder.build());
    }

    private void displayEmergencyNearbyNotification(PostNotificationMessage notificationMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXTRA_POST_ID", notificationMessage.getPostId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MainActivity.RC_NEW_POST_NOTIFICATION,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ManifestoApplication.getContext(), Constants.POST_NOTIFICATIONS_CHANNEL)
                .setContentTitle("Emergency situation near you!")
                .setContentText(notificationMessage.getMessage())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_feed)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{500, 400, 200, 200, 200, 200, 200, 200, 200})
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(POST_NOTIFICATION_ID, builder.build());
    }

    BroadcastReceiver switchBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                generateBindingKeysAndBindQueues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}

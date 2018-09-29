package com.example.stefan.manifesto.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.stefan.manifesto.model.UserLocation;
import com.example.stefan.manifesto.repository.UserRepository;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LocationTrackingService extends Service {

    private final int LOCATION_REQUEST_FREQUENCY = 5000;        //5 sec

    private FusedLocationProviderClient client;
    private LocationCallback locationCallback;

    private UserRepository userRepository = new UserRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        startGettingLocationUpdates();
    }

    @Override
    public void onDestroy() {
        client.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("MissingPermission")  //permission request is before starting service (settings activity)
    public void startGettingLocationUpdates() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_FREQUENCY);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = LocationServices.getFusedLocationProviderClient(this);

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {

                updateUserLocation(new UserLocation(UserSession.getUser().getId(), locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude()));

            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    private void updateUserLocation(UserLocation userLocation) {
        userRepository.updateUserLocation(userLocation, new SingleObserver<Void>() {
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

}
package com.example.stefan.manifesto.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityMapBinding;
import com.example.stefan.manifesto.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    public static final int RC_SELECT_ROUTE = 10;
    public static final int RC_SELECT_POST_LOCATION = 11;
    public static final String EXTRA_SELECTED_POINTS = "EXTRA_SELECTED_POINTS";
    public static final String EXTRA_SELECTED_POST_LOCATION = "EXTRA_SELECTED_POST_LOCATION";

    public static final String SELECTION_TYPE = "SELECTION_TYPE";
    public static final int TYPE_SELECT_ESCAPE_ROUTE = 20;
    public static final int TYPE_SELECT_POST_LOCATION = 21;
    public static final String PREVIOUSLY_SELECTED_ROUTE = "PREVIOUSLY_SELECTED_ROUTE";
    public static final String PREVIOUSLY_SELECTED_LOCATION = "PREVIOUSLY_SELECTED_LOCATION";

    private GoogleMap mMap;
    private Polyline polyline;
    private Marker marker;
    private ActivityMapBinding binding;
    private MapViewModel viewModel;

    private LatLng selectedLocation;
    private ArrayList<LatLng> selectedPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        binding.setViewModel(viewModel);

        if (getIntent() != null && getIntent().getIntExtra(SELECTION_TYPE, -123) != -123) {
            viewModel.setIsRouteSelection(getIntent().getIntExtra(SELECTION_TYPE, -123) == TYPE_SELECT_ESCAPE_ROUTE);
            selectedPoints = getIntent().getParcelableArrayListExtra(PREVIOUSLY_SELECTED_ROUTE);
            selectedLocation = getIntent().getParcelableExtra(PREVIOUSLY_SELECTED_LOCATION);
        }
        setUpViews();
        setUpObservers();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setUpViews() {
        initToolbar(viewModel.isRootSelectionType() ? "Set escape route" : "Set post location");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpObservers() {
        viewModel.getResetButton().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                selectedPoints.clear();
                polyline.remove();
            }
        });

        viewModel.getSaveButton().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (viewModel.isRootSelectionType()) {
                    Intent i = new Intent();
                    i.putExtra(EXTRA_SELECTED_POINTS, selectedPoints);
                    setResult(RESULT_OK, i);
                } else {
                    Intent i = new Intent();
                    i.putExtra(EXTRA_SELECTED_POST_LOCATION, marker.getPosition());
                    setResult(RESULT_OK, i);
                }
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermissionAndSetMyLocationMarker();
        drawPreviousRouteAndLocation();

        if (viewModel.isRootSelectionType()) {
            setMapClickListenerRouteSelectionMode();
        } else {
            setMapClickListenerPostLocationSelectionMode();
        }
    }

    private void drawPreviousRouteAndLocation() {
        if (selectedLocation != null) {
            if (marker != null) marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(selectedLocation).title("Post location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, mMap.getCameraPosition().zoom));
        }
        if (selectedPoints != null && selectedPoints.size() > 0) {
            polyline = mMap.addPolyline(new PolylineOptions().color(Color.RED).width(10).addAll(selectedPoints));
        }
    }

    private void setMapClickListenerRouteSelectionMode() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline != null) {
                    polyline.remove();
                }
                selectedPoints.add(latLng);
                polyline = mMap.addPolyline(new PolylineOptions().color(Color.RED).width(10).addAll(selectedPoints));
            }
        });
    }

    private void setMapClickListenerPostLocationSelectionMode() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Post location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setMyLocationMarker() {
        LatLng postLocation;
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        if (locationManager != null) {
             Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                showNoLocationInfo();
                postLocation = new LatLng(43.3327, 21.9020);
            } else {
                postLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMyLocationEnabled(true);
            }
            marker = mMap.addMarker(new MarkerOptions().position(postLocation).title("My current position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postLocation, 15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setMyLocationMarker();
                }
            }
        }
    }

    private void showNoLocationInfo() {
        makeToast("Can't find your location. Please turn on GPS.");
    }

    private void checkPermissionAndSetMyLocationMarker() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            setMyLocationMarker();
        }
    }


}

package com.example.stefan.manifesto.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;

import com.example.stefan.manifesto.MapActivity;
import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityAddPostBinding;
import com.example.stefan.manifesto.viewmodel.AddPostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddPostActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    private AddPostViewModel viewModel;
    private ActivityAddPostBinding binding;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        viewModel = ViewModelProviders.of(this).get(AddPostViewModel.class);
        binding.setViewModel(viewModel);

        initViews();
        setUpObservers();
    }

    private void setUpObservers() {
        viewModel.getBtnAddEscapeRoute().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                navigateToActivityForResult(MapActivity.class, MapActivity.RC_SELECT_ROUTE);
            }
        });

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Add post");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            setMyLocationMarker();
        }
    }

    private void setMyLocationMarker() {
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("My current position"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_ACCESS_FINE_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setMyLocationMarker();
                }
                return;
            }
        }
    }
}

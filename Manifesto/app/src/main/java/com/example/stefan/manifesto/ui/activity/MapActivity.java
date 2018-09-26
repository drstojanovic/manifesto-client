package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityMapBinding;
import com.example.stefan.manifesto.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    public static final int RC_SELECT_ROUTE = 1;

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private MapViewModel viewModel;
    private Polyline polyline;
    private ArrayList<LatLng> selectedPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        binding.setViewModel(viewModel);

        setUpViews();
        setUpObservers();
    }

    private void setUpViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Define escape route");

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline != null) {
                    polyline.remove();
                }

                PolylineOptions polylineOptions = new PolylineOptions().color(Color.RED).width(10);

                selectedPoints.add(latLng);
                polylineOptions.addAll(selectedPoints);

                polyline = mMap.addPolyline(polylineOptions);
            }
        });

    }
}

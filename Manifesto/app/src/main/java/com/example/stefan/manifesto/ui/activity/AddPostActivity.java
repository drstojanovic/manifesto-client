package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

public class AddPostActivity extends BaseActivity implements OnMapReadyCallback{

    private AddPostViewModel viewModel;
    private ActivityAddPostBinding binding;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        viewModel = ViewModelProviders.of(this).get(AddPostViewModel.class);
        binding.setViewModel(viewModel);

        initMap();
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

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

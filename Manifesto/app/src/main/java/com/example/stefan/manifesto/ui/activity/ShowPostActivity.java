package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityShowPostBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.ui.fragment.ScrollableMapFragment;
import com.example.stefan.manifesto.viewmodel.ShowPostViewModel;
import com.example.stefan.manifesto.viewmodel.ShowPostViewModelFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShowPostActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

    private ActivityShowPostBinding binding;
    private ShowPostViewModel viewModel;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_post);
        viewModel = ViewModelProviders.of(this, new ShowPostViewModelFactory(getIntent().getIntExtra(EXTRA_POST_ID, 0))).get(ShowPostViewModel.class);
        binding.setViewModel(viewModel);

        initToolbar();
        setUpObservers();
        setUpMap();
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        ((ScrollableMapFragment) mapFragment).setListener(new ScrollableMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                binding.scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    private void setUpObservers() {
        viewModel.getPost().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                drawObjectsOnMap(viewModel.getPost().get());
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Post");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void drawObjectsOnMap(Post post) {
        if (googleMap == null) return;

        LatLng postLocation = new LatLng(post.getLatitude(), post.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(postLocation));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(postLocation, 15));


        if (post.getEscapeRoute() != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<LatLng>>() {}.getType();
            ArrayList<LatLng> escapeRoute = gson.fromJson(post.getEscapeRoute(), type);
            if (escapeRoute != null && escapeRoute.size() > 0) {
                googleMap.addPolyline(new PolylineOptions().color(Color.RED).width(10).addAll(escapeRoute));
            }
        }
    }
}

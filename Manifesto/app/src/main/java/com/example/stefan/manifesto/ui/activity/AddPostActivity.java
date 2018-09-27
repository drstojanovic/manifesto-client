package com.example.stefan.manifesto.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityAddPostBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.viewmodel.AddPostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.stefan.manifesto.ManifestoApplication.getContext;

public class AddPostActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String POST_TYPE = "postType";
    public static final int REGULAR_TYPE = 1;
    public static final int EMERGENCY_TYPE = 2;

    private AddPostViewModel viewModel;
    private ActivityAddPostBinding binding;
    private GoogleMap googleMap;
    private Marker marker;
    private Polyline polyline;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        viewModel = ViewModelProviders.of(this).get(AddPostViewModel.class);
        binding.setViewModel(viewModel);

        if (getIntent() != null) {
            if (getIntent().getIntExtra(POST_TYPE, 0) == EMERGENCY_TYPE) {
                binding.btnAddEscapeRoute.setVisibility(View.VISIBLE);
                viewModel.setEmergencyType(true);
            }
        }
        initViews();
        setUpObservers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitivity_add_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_post:
                if (viewModel.getPostLocation() == null) {
                    makeToast("Please select post location on map.");
                    break;
                }
                viewModel.createPost();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpObservers() {
        viewModel.getBtnAddPostLocation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Intent intent = new Intent(AddPostActivity.this, MapActivity.class);
                intent.putExtra(MapActivity.PREVIOUSLY_SELECTED_LOCATION, viewModel.getPostLocation());
                intent.putParcelableArrayListExtra(MapActivity.PREVIOUSLY_SELECTED_ROUTE, (ArrayList<? extends Parcelable>) viewModel.getSelectedEscapeRoutePoints());
                intent.putExtra(MapActivity.SELECTION_TYPE, MapActivity.TYPE_SELECT_POST_LOCATION);
                startActivityForResult(intent, MapActivity.RC_SELECT_POST_LOCATION);
            }
        });

        viewModel.getBtnAddEscapeRoute().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Intent intent = new Intent(AddPostActivity.this, MapActivity.class);
                intent.putExtra(MapActivity.PREVIOUSLY_SELECTED_LOCATION, viewModel.getPostLocation());
                intent.putParcelableArrayListExtra(MapActivity.PREVIOUSLY_SELECTED_ROUTE, (ArrayList<? extends Parcelable>) viewModel.getSelectedEscapeRoutePoints());
                intent.putExtra(MapActivity.SELECTION_TYPE, MapActivity.TYPE_SELECT_ESCAPE_ROUTE);
                startActivityForResult(intent, MapActivity.RC_SELECT_ROUTE);
            }
        });

        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (events == null || events.size() == 0) {
                    makeToast("No events followed");
                    finish();
                }
                setSpinner(events);
            }
        });

        viewModel.getCreationResponse().observe(this, new Observer<ResponseMessage<Post>>() {
            @Override
            public void onChanged(@Nullable ResponseMessage<Post> response) {
                if (response != null && response.getMessage() != null) {
                    makeToast(response.getMessage());
                }
            }
        });

        viewModel.getBtnTakePicture().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                captureImageIntent();
            }
        });

        viewModel.getBtnSelectPicture().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                startActivityForResult(new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT), AddPostViewModel.RC_GALLERY);
            }
        });

        viewModel.getImageUrl().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.imageSelectedImage.setVisibility(View.VISIBLE);
                binding.textImage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(viewModel.isEmergencyType() ? "Add emergency post" : "Add post");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        binding.spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) return;
                viewModel.setSelectedEvent((Event) binding.spinnerEvent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng initialPosition = new LatLng(43.3327, 21.9020);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 15));
    }

    private void setSpinner(List<Event> events) {
        binding.spinnerEvent.setAdapter(new ArrayAdapter<Event>(this, android.R.layout.simple_spinner_dropdown_item, events));
        binding.spinerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerEvent.performClick();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case AddPostViewModel.RC_CAMERA:
            case AddPostViewModel.RC_GALLERY:
                viewModel.addNewImage(requestCode, data);
                break;
            case MapActivity.RC_SELECT_ROUTE:
                ArrayList<LatLng> list = data.getParcelableArrayListExtra(MapActivity.EXTRA_SELECTED_POINTS);
                viewModel.setSelectedRoutePoints(list);
                drawRouteOnMap(list);
                break;
            case MapActivity.RC_SELECT_POST_LOCATION:
                LatLng postLocation = data.getParcelableExtra(MapActivity.EXTRA_SELECTED_POST_LOCATION);
                viewModel.setPostLocation(postLocation);
                drawMarkerOnMap(postLocation);
                break;
        }
    }

    private void drawMarkerOnMap(LatLng postLocation) {
        if (marker != null) marker.remove();
        marker = googleMap.addMarker(new MarkerOptions().position(postLocation).title("Post location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(postLocation, 15));
    }

    private void drawRouteOnMap(ArrayList<LatLng> list) {
        if (polyline != null) polyline.remove();
        if (list == null || list.size() == 0) return;
        polyline = googleMap.addPolyline(new PolylineOptions().width(10).color(Color.RED).addAll(list));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 15));
    }

    private void captureImageIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = viewModel.createImageFile();
            } catch (IOException e) {
                Log.e("FileError", "Error while trying to create a image file");
            }
            if (photoFile != null) {
                viewModel.setCapturedImageUri(FileProvider.getUriForFile(AddPostActivity.this, getString(R.string.file_provider_authority), photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.getCapturedImageUri());
                startActivityForResult(takePictureIntent, AddPostViewModel.RC_CAMERA);
            }
        }
    }

}

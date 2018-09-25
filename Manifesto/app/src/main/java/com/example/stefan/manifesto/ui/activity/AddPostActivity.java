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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.widget.Spinner;

import com.example.stefan.manifesto.MapActivity;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.example.stefan.manifesto.ManifestoApplication.getContext;

public class AddPostActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    private AddPostViewModel viewModel;
    private ActivityAddPostBinding binding;
    private GoogleMap googleMap;
    private LatLng postLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        viewModel = ViewModelProviders.of(this).get(AddPostViewModel.class);
        binding.setViewModel(viewModel);

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
                viewModel.createPost(postLocation);
                break;
            case R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpObservers() {
        viewModel.getBtnAddEscapeRoute().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                navigateToActivityForResult(MapActivity.class, MapActivity.RC_SELECT_ROUTE);
            }
        });

        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (events == null || events.size() == 0){
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
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        .setAction(Intent.ACTION_GET_CONTENT), AddPostViewModel.RC_GALLERY);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            setMyLocationMarker();
        }
    }

    @SuppressLint("MissingPermission")
    private void setMyLocationMarker() {
        LocationManager locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                showNoLocationInfo();
                postLocation = new LatLng(43.3327, 21.9020);
            } else {
                postLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
            googleMap.addMarker(new MarkerOptions().position(postLocation).title("My current position"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postLocation, 15));
        }
    }

    private void showNoLocationInfo() {
        makeToast("Can't find your location. Please turn on GPS.");
    }

    private void setSpinner(List<Event> events) {
        binding.spinnerEvent.setAdapter(new ArrayAdapter<Event>(this, android.R.layout.simple_spinner_item, events));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            viewModel.addNewImage(requestCode, data);
        }
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

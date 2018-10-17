package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.repository.PostRepository;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.FirebaseOperations;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.support.constraint.Constraints.TAG;

public class AddPostViewModel extends BaseViewModel {

    public static final int RC_CAMERA = 1;
    public static final int RC_GALLERY = 2;

    private MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private ObservableField<String> imageUrl = new ObservableField<>();
    private SingleLiveEvent<Boolean> btnTakePicture = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> btnSelectPicture = new SingleLiveEvent<>();
    private ObservableField<Post> post = new ObservableField<>();
    private MutableLiveData<ResponseMessage<Post>> creationResponse = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> btnAddEscapeRoute = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> btnAddPostLocation = new SingleLiveEvent<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private Event postEvent;
    private EventRepository eventRepository = new EventRepository();
    private PostRepository postRepository = new PostRepository();
    private Uri capturedImageUri;
    private List<Uri> imageUris = new ArrayList<>();
    private boolean isEmergencyType;
    private LatLng postLocation;
    private List<LatLng> escapeRoute = new ArrayList<>();

    public AddPostViewModel() {
        post.set(new Post());
        postEvent = new Event();
        loadFollowedEvents();
    }

    private void loadFollowedEvents() {
        eventRepository.getFollowedEventsOfCurrentUser(new SingleObserver<List<Event>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Event> list) {
                if (list != null && list.size() > 0) {
                    events.setValue(list);
                    postEvent = list.get(0);
                } else {
                    errorMessage.setValue("Must follow events before creating post.");
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void createPost() {
        if (postLocation == null) {
            creationResponse.setValue(new ResponseMessage<Post>(false, "Please select post location on map."));
            return;
        }

        postRepository.createPost(extractPost(), new SingleObserver<ResponseMessage<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ResponseMessage<Post> postResponseMessage) {
                if (postResponseMessage != null && postResponseMessage.isSuccess() && postResponseMessage.getResponseBody() != null) {
                    postResponseMessage.setSuccess(false);
                    saveImages(postResponseMessage.getResponseBody());
                }
            }

            @Override
            public void onError(Throwable e) {
                creationResponse.setValue(new ResponseMessage<Post>(false, "Error happened during saving."));
            }
        });
    }

    private void saveImages(final Post savedPost) {
        if (imageUris == null || imageUris.size() == 0) {
            creationResponse.setValue(new ResponseMessage<Post>(true, "Post created"));
            return;
        }

        FirebaseOperations.getInstance().getStorageReference()
                .child(Constants.FIREBASE_POSTS)
                .child(String.valueOf(savedPost.getId()))
                .child("img" + 0).putFile(imageUris.get(0))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        saveImageUrlInDatabase(savedPost);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        creationResponse.setValue(new ResponseMessage<Post>(false, "Error happened during saving."));
                    }
                });
    }

    private void saveImageUrlInDatabase(final Post savedPost) {
        FirebaseOperations.getInstance().getStorageReference()
                .child(Constants.FIREBASE_POSTS)
                .child(String.valueOf(savedPost.getId()))
                .child("img0")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        savedPost.setImage(uri.toString());
                        postRepository.updatePost(savedPost,
                                new SingleObserver<ResponseMessage<Post>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(ResponseMessage<Post> postResponseMessage) {
                                        creationResponse.setValue(new ResponseMessage<Post>(true, "Post created"));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        creationResponse.setValue(new ResponseMessage<Post>(false, "Error happened during saving."));
                                    }
                                });
                    }
                });
    }


    /**
     * ============== BUTTONS ======================
     */
    public void onTakePictureButtonClick() {
        btnTakePicture.setValue(true);
    }

    public void onSelectPictureButtonClick() {
        btnSelectPicture.setValue(true);
    }

    public void onAddPostLocationButtonClick() {
        btnAddPostLocation.setValue(true);
    }

    public void onAddEscapeRouteButtonClick() {
        btnAddEscapeRoute.setValue(true);
    }

    public void setSelectedEvent(Event event) {
        postEvent = event;
    }

    /**
     * ==================== HELPER FUNCTIONS =======================
     */

    private Post extractPost() {
        Post p = post.get();
        if (p == null) {
            p = new Post();
            p.setText("");
        }

        p.setEventId(postEvent.getId());
        p.setEventName(postEvent.getName());
        p.setLatitude(getPostLocation().latitude);
        p.setLongitude(getPostLocation().longitude);
        p.setTime(DateTime.now().toDate());
        p.setType(isEmergencyType ? Post.EMERGENCY_TYPE : Post.REGULAR_TYPE);
        p.setUser(UserSession.getUser());
        Gson gson = new Gson();
        p.setEscapeRoute(gson.toJson(escapeRoute));
        return p;
    }

    public File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String filename = "IMG_" + timestamp;
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Manifesto");
        imagesFolder.mkdir();
        return File.createTempFile(filename, ".jpg", imagesFolder);
    }

    public void addNewImage(int requestCode, Intent data) {
        if (requestCode == RC_CAMERA) {
            imageUris.add(capturedImageUri);
            imageUrl.set(capturedImageUri.toString());
        } else if (requestCode == RC_GALLERY) {
            if (data != null) {
                if (data.getClipData() != null) {   //multiple images selected
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        imageUris.add(data.getClipData().getItemAt(i).getUri());
                        imageUrl.set(data.getClipData().getItemAt(i).getUri().toString());
                    }
                } else {                        //single image selected
                    imageUris.add(data.getData());
                    imageUrl.set(data.getData().toString());
                }
            }
        }
    }

    /**
     * =================== GETTERS AND SETTERS ===========================
     */

    public Uri getCapturedImageUri() {
        return capturedImageUri;
    }

    public void setCapturedImageUri(Uri capturedImageUri) {
        this.capturedImageUri = capturedImageUri;
    }

    public ObservableField<Post> getPost() {
        return post;
    }

    public String getText() {
        return post.get().getText();
    }

    public void setText(String text) {
        post.get().setText(text);
    }

    public ObservableField<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ObservableField<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isEmergencyType() {
        return isEmergencyType;
    }

    public void setEmergencyType(boolean emergencyType) {
        isEmergencyType = emergencyType;
    }

    public LiveData<Boolean> getBtnAddEscapeRoute() {
        return btnAddEscapeRoute;
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<ResponseMessage<Post>> getCreationResponse() {
        return creationResponse;
    }

    public LiveData<Boolean> getBtnSelectPicture() {
        return btnSelectPicture;
    }

    public LiveData<Boolean> getBtnTakePicture() {
        return btnTakePicture;
    }

    public LiveData<Boolean> getBtnAddPostLocation() {
        return btnAddPostLocation;
    }

    public void setSelectedRoutePoints(ArrayList<LatLng> list) {
        this.escapeRoute = list;
    }

    public List<LatLng> getSelectedEscapeRoutePoints() {
        return this.escapeRoute;
    }

    public void setPostLocation(LatLng postLocation) {
        this.postLocation = new LatLng(postLocation.latitude, postLocation.longitude);
    }

    public LatLng getPostLocation() {
        return postLocation;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(MutableLiveData<String> errorMessage) {
        this.errorMessage = errorMessage;
    }
}

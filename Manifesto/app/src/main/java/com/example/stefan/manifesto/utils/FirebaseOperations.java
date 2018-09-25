package com.example.stefan.manifesto.utils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseOperations {

    private static final FirebaseOperations instance = new FirebaseOperations();

    private StorageReference storageReference;

    private FirebaseOperations() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseOperations getInstance() {
        return instance;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}

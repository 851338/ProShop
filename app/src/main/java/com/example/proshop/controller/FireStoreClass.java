package com.example.proshop.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.proshop.activities.AddNewActivity;
import com.example.proshop.activities.LoginActivity;
import com.example.proshop.activities.ProfileActivity;
import com.example.proshop.activities.RegisterActivity;
import com.example.proshop.model.Product;
import com.example.proshop.model.User;
import com.example.proshop.menu.SettingActivity;
import com.example.proshop.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class FireStoreClass {

    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    LoginActivity loginActivity;
    SettingActivity settingActivity;
    ProfileActivity profileActivity;
    AddNewActivity addNewActivity;

    public void registerUser(RegisterActivity activity, User userInfo) {
        fireStore.collection(Constants.USERS)
                .document(userInfo.getId())

                .set(userInfo, SetOptions.merge())

                .addOnSuccessListener(aVoid -> activity.userCreatedSuccessfully())

                .addOnFailureListener(e -> {
                    activity.hideProgressBar();
                    Log.e("", "Create user failed!", e);
                });
    }

    public String getCurrentUID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = "";
        if (currentUser != null) {
            currentUID = currentUser.getUid();
        }
        return currentUID;
    }

    public void getUserDetails(Activity activity) {
        fireStore.collection(Constants.USERS)
                .document(getCurrentUID())
                .get()
                .addOnSuccessListener(documentSnapshot ->  {
                    Log.i("", documentSnapshot.toString());
                    User user = documentSnapshot.toObject(User.class);
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(
                            Constants.MY_APP_PREFERENCES,
                            Context.MODE_PRIVATE
                    );

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    assert user != null;
                    editor.putString(
                            Constants.LOGGED_IN_USERNAME,
                            user.getFirstName() + " " + user.getLastName()
                    );
                    editor.putString(Constants.LOGGED_IN_EMAIL,
                            user.getEmail()
                    );
                    editor.putLong(
                            Constants.LOGGED_IN_PHONE,
                            user.getPhone()
                    );
                    editor.apply();
                    
                    if (activity.getClass().getSimpleName().equals("LoginActivity")) {
                        loginActivity = (LoginActivity) activity;
                        loginActivity.userLoggedInSuccess();
                    }
                    if (activity.getClass().getSimpleName().equals("SettingActivity")) {
                        settingActivity = (SettingActivity) activity;
                        settingActivity.getUserDetailsSuccess(user);
                    }
                    if (activity.getClass().getSimpleName().equals("AddNewActivity")) {
                        addNewActivity = (AddNewActivity) activity;
                        addNewActivity.getUserDetailsSuccess(user);
                    }
                })

                .addOnFailureListener(e -> {
                    if (activity.getClass().getSimpleName().equals("LoginActivity")) {
                        loginActivity = (LoginActivity) activity;
                        loginActivity.hideProgressBar();
                        Log.e(activity.getClass().getSimpleName(),
                                "Logged in failed due to exception.", e);
                    }
                    if (activity.getClass().getSimpleName().equals("SettingActivity")) {
                        settingActivity = (SettingActivity) activity;
                        settingActivity.hideProgressBar();
                        Log.e(activity.getClass().getSimpleName(),
                                "Get user details failed due to exception.", e);
                    }

                });
    }

    public void addProduct(AddNewActivity activity, @NotNull Product product) {
        fireStore.collection(Constants.LAPTOPS)
                .document(product.getId())

                .set(product, SetOptions.merge())

                .addOnSuccessListener(aVoid -> activity.productAddedSuccessfully())

                .addOnFailureListener(e -> {
                    activity.hideProgressBar();
                    Log.e("", "Add new product failed!", e);
                });
    }

    public void updateUserProfile(Activity activity, HashMap<String, Object> userHashMap) {
        fireStore.collection(Constants.USERS)
                .document(getCurrentUID())
                .update(userHashMap)
                .addOnSuccessListener(task -> {
                    if(activity.getClass().getSimpleName().equals("ProfileActivity")) {
                        profileActivity = (ProfileActivity) activity;
                        profileActivity.profileUpdateSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if(activity.getClass().getSimpleName().equals("ProfileActivity")) {
                        profileActivity = (ProfileActivity) activity;
                        profileActivity.hideProgressBar();
                    }
                    Log.e(activity.getClass().getSimpleName(),
                            "Error while updating profile details", e);
                });
    }

    public void uploadImageToFireStorage(Activity activity, Uri imgUri) {
        Constants constants = new Constants();

        StorageReference sRef = FirebaseStorage.getInstance().getReference().child(
                Constants.UPLOADED_IMG +System.currentTimeMillis() + "." +
                        constants.getFileExtension(activity, imgUri));

        sRef.putFile(imgUri)
                .addOnSuccessListener(taskSnapshot -> {
//                    DatabaseReference dRef =
//                        FirebaseDatabase.getInstance(
//                                "https://proshop-53232-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                                .getReference("images");
//                    mUserImage = taskSnapshot.getMetadata()
//                            .getReference().getDownloadUrl().toString();
//                    Image img = new Image(mUserImage);
//                    String imgId = dRef.push().getKey();
//                    dRef.child(imgId).setValue(img);
                    Objects.requireNonNull(Objects.requireNonNull(
                            taskSnapshot.getMetadata()).getReference()).getDownloadUrl()
                            .addOnSuccessListener( uri -> {
                                if(activity.getClass().getSimpleName().equals("ProfileActivity")) {
                                    Log.e("Downloadable Image", uri.toString());
                                    profileActivity = (ProfileActivity) activity;
                                    profileActivity.imgUploadSuccess(uri.toString());
                                }

                                if(activity.getClass().getSimpleName().equals("AddNewActivity")) {
                                    Log.e("Downloadable Image", uri.toString());
                                    addNewActivity = (AddNewActivity) activity;
                                    addNewActivity.imgUploadSuccess(uri.toString());
                                }
                            });

                })

                .addOnFailureListener(e -> {
                    if(activity.getClass().getSimpleName().equals("ProfileActivity")) {
                        profileActivity = (ProfileActivity) activity;
                        profileActivity.hideProgressBar();
                    }
                    if(activity.getClass().getSimpleName().equals("AddNewActivity")) {
                        addNewActivity = (AddNewActivity) activity;
                        addNewActivity.hideProgressBar();
                    }
                    Log.e(activity.getClass().getSimpleName(), e.getMessage(), e);
                });
    }
}



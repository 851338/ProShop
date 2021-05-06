package com.example.proshop.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public class Constants {
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_LAST_NAME = "lastName";
    public static final int READ_STORAGE_PERMISSION_CODE = 2;
    public static final int PICK_IMG_REQUEST_CODE = 2;
    public static final String USERS = "users";
    public static final String LAPTOPS = "laptops";
    public static final String LOGGED_IN_PHONE = "phone";
    public static final String LOGGED_IN_GENDER = "gender";
    public static final String USER_PROFILE_IMG = "image";

    public static final String MY_APP_PREFERENCES = "MyAppPrefs";
    public static final String LOGGED_IN_USERNAME = "Logged_in_username";
    public static final String PROFILE_COMPLETED = "Profile_completed";
    public static final String LOGGED_IN_EMAIL = "Logged_in_email";
    public static final String EXTRA_USER_DETAIL = "Extra_user_detail";
    public static final String UPLOADED_IMG = "Upload_profile_image";

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";

    public void showImgChooser(Activity activity) {
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        activity.startActivityForResult(galleryIntent, PICK_IMG_REQUEST_CODE);
    }

    public String getFileExtension(Activity activity, Uri uri) {

        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.getContentResolver().getType(uri));
    }
}

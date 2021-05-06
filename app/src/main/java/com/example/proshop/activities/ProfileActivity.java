package com.example.proshop.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proshop.R;
import com.example.proshop.controller.FireStoreClass;
import com.example.proshop.model.User;
import com.example.proshop.utils.Constants;
import com.example.proshop.utils.GlideLoader;

import java.util.HashMap;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private User userDetails;
    private Uri mSelectedImageUri = null;
    private String mUserProfileImg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        setupActionBar();
        autoSetField();

        if(getIntent().hasExtra(Constants.EXTRA_USER_DETAIL)) {
            userDetails = getIntent().getParcelableExtra(Constants.EXTRA_USER_DETAIL);
        }

        Button save = findViewById(R.id.btn_pro_save);
        ImageView imgV = findViewById(R.id.pro_user_img);
        save.setOnClickListener(this);
        imgV.setOnClickListener(this);
    }

    private void setupActionBar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarPro);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bbarow_24);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.app_gradient_color_bg)
        );

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void autoSetField() {
        EditText profileEmail = findViewById(R.id.pro_email);
        EditText profileFirstName = findViewById(R.id.pro_firstName);
        EditText profileLastName = findViewById(R.id.pro_lastName);
        EditText profilePhone = findViewById(R.id.pro_phone);
        Button maleRb = findViewById(R.id.rb_male);
        Button femaleRb = findViewById(R.id.rb_female);
        ImageView profileUserImg = findViewById(R.id.pro_user_img);

        if(getIntent().hasExtra(Constants.EXTRA_USER_DETAIL)) {
            userDetails = getIntent().getParcelableExtra(Constants.EXTRA_USER_DETAIL);
        } else {
            userDetails = new User();
        }

        GlideLoader glideLoader = new GlideLoader();

        if(userDetails.getImage() != null) {
            glideLoader.loadUserPicture(userDetails.getImage(), profileUserImg, this);
        }
        if(userDetails.getPhone() != 0L) {
            profilePhone.setText(userDetails.getPhone().toString());
        }
        if(userDetails.getGender().equals(Constants.MALE)) {
            maleRb.isSelected();
        } else {
            femaleRb.isSelected();
        }

        profileEmail.setText(userDetails.getEmail());
        profileFirstName.setText(userDetails.getFirstName());
        profileLastName.setText(userDetails.getLastName());
    }

    @Override
    public void onClick(View v) {
        if(v != null) {
            if(v.getId() == R.id.pro_user_img) {
                if(ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                    Constants constants = new Constants();
                    constants.showImgChooser(this);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.READ_STORAGE_PERMISSION_CODE);
                }
            }

            if(v.getId() == R.id.btn_pro_save) {
                if (validateUserProfileData()) {
                    showProgressBar();

                    if(mSelectedImageUri != null) {
                        FireStoreClass fireStoreClass = new FireStoreClass();
                        fireStoreClass.uploadImageToFireStorage(this, mSelectedImageUri);
                    }
                    updateUserProfileDetails();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if(grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants constants = new Constants();
                constants.showImgChooser(this);
            } else {
                Toast.makeText(this, getString(R.string.permission_denied),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Constants.PICK_IMG_REQUEST_CODE) {
                if(data != null) {
                    ImageView profileUserImg = findViewById(R.id.pro_user_img);
                    mSelectedImageUri = data.getData();
                    GlideLoader glideLoader = new GlideLoader();

                    glideLoader.loadUserPicture(mSelectedImageUri.toString(), profileUserImg, this);
                }
            }
        }
    }

    private Boolean validateUserProfileData() {

        EditText profileFirstName = findViewById(R.id.pro_firstName);
        EditText profileLastName = findViewById(R.id.pro_lastName);
        EditText profilePhone = findViewById(R.id.pro_phone);

        if(TextUtils.isEmpty(profilePhone.getText().toString().trim())) {
            showErrorSnackBar(getString(R.string.err_msg_enter_phone_number), true);
            return false;
        }
        if(TextUtils.isEmpty(profileFirstName.getText().toString().trim())) {
            showErrorSnackBar(getString(R.string.err_msg_enter_phone_number), true);
            return false;
        }
        if(TextUtils.isEmpty(profileLastName.getText().toString().trim())) {
            showErrorSnackBar(getString(R.string.err_msg_enter_phone_number), true);
            return false;
        }
         return true;
    }

    private void updateUserProfileDetails() {

        EditText profileFirstName = findViewById(R.id.pro_firstName);
        EditText profileLastName = findViewById(R.id.pro_lastName);
        EditText profilePhone = findViewById(R.id.pro_phone);
        Button maleRb = findViewById(R.id.rb_male);

        HashMap<String, Object> userHashMap = new HashMap<>();

        String gender;
        String firstName = profileFirstName.getText().toString().trim();
        String lastName = profileLastName.getText().toString().trim();
        String phone = profilePhone.getText().toString().trim();
        Long phoneNumber = Long.parseLong(phone);

        if(maleRb.isSelected()) {
            gender = Constants.MALE;
        } else {
            gender = Constants.FEMALE;
        }

        userHashMap.put(Constants.USER_FIRST_NAME, firstName);
        userHashMap.put(Constants.USER_LAST_NAME, lastName);
        userHashMap.put(Constants.USER_PROFILE_IMG, mUserProfileImg);
        userHashMap.put(Constants.LOGGED_IN_PHONE, phoneNumber);
        userHashMap.put(Constants.LOGGED_IN_GENDER, gender);

        FireStoreClass fireStoreClass = new FireStoreClass();
        fireStoreClass.updateUserProfile(this, userHashMap);
    }

    public void profileUpdateSuccess() {
        hideProgressBar();
        Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_LONG).show();
    }

    public void imgUploadSuccess(String imgURL) {
        mUserProfileImg = imgURL;
        updateUserProfileDetails();
    }
}
package com.example.proshop.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.proshop.model.Product;
import com.example.proshop.model.User;
import com.example.proshop.utils.Constants;
import com.example.proshop.utils.GlideLoader;

import java.util.Calendar;
import java.util.Date;

public class AddNewActivity extends BaseActivity implements View.OnClickListener{

    private Uri mSelectedImageUri = null;
    private String mUserProfileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        setupActionBar();

        ImageView imgV = findViewById(R.id.addNew_iv);
        imgV.setOnClickListener(this);
        Button addNew = findViewById(R.id.buttonSubmit);
        addNew.setOnClickListener(this);
    }

    private void setupActionBar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bbarow_24);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.app_gradient_color_bg)
        );

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public Boolean validateProductDetail() {
        EditText pName = findViewById(R.id.addNew_nameEt);
        EditText pCard = findViewById(R.id.addNew_cardTv);
        EditText pCpu = findViewById(R.id.addMew_cpuEt);
        EditText pDisk = findViewById(R.id.addNew_diskEt);
        EditText pPrice = findViewById(R.id.addNew_priceEt);
        EditText pQuantity = findViewById(R.id.addNew_quantityEt);
        EditText pRam = findViewById(R.id.addNew_ramEt);

        if (pName.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_first_name), true);
            return false;
        } else if (pCard.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_last_name), true);
            return false;
        } else if (pCpu.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_password), true);
            return false;
        } else if (pRam.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (pDisk.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (pPrice.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (pQuantity.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else {
            return true;
        }
    }

    public void addNewProduct(User seller) {

        EditText pName = findViewById(R.id.addNew_nameEt);
        EditText pCard = findViewById(R.id.addNew_cardTv);
        EditText pCpu = findViewById(R.id.addMew_cpuEt);
        EditText pDisk = findViewById(R.id.addNew_diskEt);
        EditText pPrice = findViewById(R.id.addNew_priceEt);
        EditText pQuantity = findViewById(R.id.addNew_quantityEt);
        EditText pRam = findViewById(R.id.addNew_ramEt);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        Long rightNow = System.currentTimeMillis();
        String lapId = strDate + rightNow.toString();
        String lapName = pName.getText().toString();
        String lapCard = pCard.getText().toString();
        String lapCpu = pCpu.getText().toString();
        String lapDisk = pDisk.getText().toString();
        String lapPrice = pPrice.getText().toString();
        Float price = Float.parseFloat(lapPrice);
        String lapQuantity = pQuantity.getText().toString();
        int quantity = Integer.parseInt(lapQuantity);
        String lapRam = pRam.getText().toString();

        Product product = new Product(lapId, lapName, lapCpu, mUserProfileImg,
                lapRam, lapDisk, lapCard, price, quantity, seller.getId());

        FireStoreClass fireStoreClass = new FireStoreClass();
        fireStoreClass.addProduct(this, product);
    }

    public void getUserDetailsSuccess(User user) {
        addNewProduct(user);
    }

    private void getUserDetails() {
        FireStoreClass fireStoreClass = new FireStoreClass();
        fireStoreClass.getUserDetails(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonSubmit) {
            if(validateProductDetail()) {
                showProgressBar();
                if(mSelectedImageUri == null) {
                    hideProgressBar();
                    Toast.makeText(this, getString(R.string.err_msg_image_not_chosen) , Toast.LENGTH_SHORT).show();
                } else {
                    FireStoreClass fireStoreClass = new FireStoreClass();
                    fireStoreClass.uploadImageToFireStorage(this, mSelectedImageUri);
                }
            }
        }
        if(v.getId() == R.id.addNew_iv) {
            if(ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Constants constants = new Constants();
                constants.showImgChooser(this);
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.READ_STORAGE_PERMISSION_CODE);
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
                    ImageView profileUserImg = findViewById(R.id.addNew_iv);
                    mSelectedImageUri = data.getData();
                    GlideLoader glideLoader = new GlideLoader();

                    glideLoader.loadUserPicture(mSelectedImageUri.toString(), profileUserImg, this);
                }
            }
        }
    }

    public void productAddedSuccessfully() {
        hideProgressBar();
        Toast.makeText(this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
    }

    public void imgUploadSuccess(String imgURL) {
        mUserProfileImg = imgURL;
        getUserDetails();
    }
}
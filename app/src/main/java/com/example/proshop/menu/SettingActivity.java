package com.example.proshop.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.proshop.R;
import com.example.proshop.activities.BaseActivity;
import com.example.proshop.activities.LoginActivity;
import com.example.proshop.activities.ProfileActivity;
import com.example.proshop.controller.FireStoreClass;
import com.example.proshop.model.User;
import com.example.proshop.utils.Constants;
import com.example.proshop.utils.GlideLoader;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private User mUserDetails;

    public SettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setupActionBar();
        TextView editTv = findViewById(R.id.tv_edit);
        editTv.setOnClickListener(this);

        Button logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(this);

        Button addPayment = findViewById(R.id.btnAddPayment);
        addPayment.setOnClickListener(this);
    }

    private void setupActionBar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarSet);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bbarow_24);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.app_gradient_color_bg)
        );

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getUserDetails() {
        showProgressBar();
        FireStoreClass fireStoreClass = new FireStoreClass();
        fireStoreClass.getUserDetails(this);
    }

    public void getUserDetailsSuccess(User user) {
        mUserDetails = user;
        ImageView iv_u = findViewById(R.id.iv_user_photo);

        hideProgressBar();
        GlideLoader glideLoader = new GlideLoader();
        glideLoader.loadUserPicture(user.getImage(), iv_u, this);

        TextView nameTv = findViewById(R.id.tv_name);
        TextView phoneTv = findViewById(R.id.tv_phone);
        TextView genderTv = findViewById(R.id.tv_gender);
        TextView emailTv = findViewById(R.id.tv_email);

        nameTv.setText(user.getFirstName() + " " + user.getLastName());
        phoneTv.setText(String.format(user.getPhone().toString()));
        genderTv.setText(user.getGender());
        emailTv.setText(user.getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if(v.getId() == R.id.btnAddPayment) {

        } else if(v.getId() == R.id.tv_edit) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra(Constants.EXTRA_USER_DETAIL, mUserDetails);
            startActivity(i);
        }
    }
}
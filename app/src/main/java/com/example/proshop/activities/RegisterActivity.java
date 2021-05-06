package com.example.proshop.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.proshop.R;
import com.example.proshop.controller.FireStoreClass;
import com.example.proshop.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseAuth.getInstance();

        setupActionBar();

        TextView redirectLogTv = findViewById(R.id.redirectLog);
        redirectLogTv.setOnClickListener(this);

        Button registerBtn = findViewById(R.id.btnReg);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.redirectLog) {
                onBackPressed();
            } else if (v.getId() == R.id.btnReg) {
                createNewUser();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setupActionBar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarReg);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bbarow_24);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.app_gradient_color_bg)
        );

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private boolean validateRegisterDetail() {
        EditText firstName = findViewById(R.id.firstNameReg);
        EditText lastName = findViewById(R.id.lastNameReg);
        EditText password = findViewById(R.id.passwordReg);
        EditText email = findViewById(R.id.emailReg);
        EditText re_password = findViewById(R.id.repasswordReg);

        if (firstName.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_first_name), true);
            return false;
        } else if (lastName.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_last_name), true);
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_password), true);
            return false;
        } else if (email.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (!(password.getText().toString().trim()).equals(re_password.getText().toString().trim())) {
            showErrorSnackBar(getString(R.string.err_msg_password_conf_password_mismatch), true);
            return false;
        } else {
            return true;
        }
    }

    private void createNewUser() {
        if (validateRegisterDetail()) {
            showProgressBar();

            EditText passwordEt = findViewById(R.id.passwordReg);
            String password = passwordEt.getText().toString().trim();

            EditText emailEt = findViewById(R.id.emailReg);
            String email = emailEt.getText().toString().trim();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {

                            EditText firstNameEt = findViewById(R.id.firstNameReg);
                            String firstName = firstNameEt.getText().toString().trim();

                            EditText lastNameEt = findViewById(R.id.lastNameReg);
                            String lastName = lastNameEt.getText().toString().trim();

                            FirebaseUser firebaseUser = Objects.requireNonNull(task.getResult()).getUser();

                            assert firebaseUser != null;
                            User user = new User(firebaseUser.getUid(), firstName, lastName, email);

                            FireStoreClass fireStoreClass = new FireStoreClass();
                            fireStoreClass.registerUser(RegisterActivity.this, user);


                            //finish();
                        } else {
                            hideProgressBar();
                            showErrorSnackBar(Objects.requireNonNull(task.getException()).getMessage(), true);
                        }
                    });
        }

    }

    public void userCreatedSuccessfully() {
        FirebaseAuth.getInstance().signOut();
        hideProgressBar();
        Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_SHORT).show();
    }
}
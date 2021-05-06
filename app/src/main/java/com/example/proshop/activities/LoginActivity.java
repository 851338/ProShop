package com.example.proshop.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proshop.R;
import com.example.proshop.controller.FireStoreClass;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
  //      FirebaseAuth auth = FirebaseAuth.getInstance();

        TextView forgotPassTv = findViewById(R.id.forgotPass);
        forgotPassTv.setOnClickListener(this);

        TextView redirectRegTv = findViewById(R.id.redirectReg);
        redirectRegTv.setOnClickListener(this);

        Button loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.forgotPass) {
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.redirectReg) {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.btnLogin) {
                loginWithCreatedUser();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean validatedLoginDetail() {
        EditText etEmailLog = findViewById(R.id.emailLog);
        EditText etPasswordLog = findViewById(R.id.passwordLog);

        if (etEmailLog.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (etPasswordLog.getText().toString().trim().isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_password), true);
            return false;
        } else {
            return true;
        }
    }

    private void loginWithCreatedUser() {
        if (validatedLoginDetail()) {
            showProgressBar();

            EditText etEmailLog = findViewById(R.id.emailLog);
            EditText etPasswordLog = findViewById(R.id.passwordLog);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    etEmailLog.getText().toString(), etPasswordLog.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FireStoreClass fireStoreClass = new FireStoreClass();
                            fireStoreClass.getUserDetails(LoginActivity.this);
                        } else {
                            hideProgressBar();
                            showErrorSnackBar(Objects.requireNonNull(task.getException()).getMessage(),true);
                        }
                    });
        }
    }

    public void userLoggedInSuccess() {
        hideProgressBar();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
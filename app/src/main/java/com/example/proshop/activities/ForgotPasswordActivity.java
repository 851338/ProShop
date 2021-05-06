package com.example.proshop.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.proshop.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setupActionBar();

        Button sendEmail = findViewById(R.id.btnSendEmail);
        sendEmail.setOnClickListener(v -> resetPassword());
    }

    private void setupActionBar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarForgot);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_bbarow_24);
        getSupportActionBar().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.app_gradient_color_bg)
        );

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void resetPassword() {
        EditText emailEt = findViewById(R.id.emailForgot);
        String email = emailEt.getText().toString().trim();

        if(email.isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true);
        } else {
            showProgressBar();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener( task -> {
                        hideProgressBar();
                        if(task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.sent_email),
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            showErrorSnackBar(Objects.requireNonNull(task.getException()).getMessage(), true);
                        }
                    });
        }
    }
}
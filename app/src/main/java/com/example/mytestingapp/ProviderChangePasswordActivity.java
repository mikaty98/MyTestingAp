package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderChangePasswordActivity extends AppCompatActivity {

    private EditText oldPass, newPass, confirmPass;
    private Button confirmBtn;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_change_password);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        confirmPass = findViewById(R.id.confirmPass);
        confirmBtn = findViewById(R.id.confirmBtn);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OldPass = oldPass.getText().toString().trim();
                String NewPass = newPass.getText().toString().trim();
                String ConfirmPass = confirmPass.getText().toString().trim();

                user = FirebaseAuth.getInstance().getCurrentUser();
                auth = FirebaseAuth.getInstance();

                boolean errorFlag = false;


                if (TextUtils.isEmpty(OldPass)) {
                    oldPass.setError("Password can not be left empty");
                    errorFlag = true;
                }

                if (OldPass.length() < 6) {
                    oldPass.setError("Password must be more than 6 characters");
                    errorFlag = true;
                }

                if (!errorFlag){
                    auth.signInWithEmailAndPassword(user.getEmail(), OldPass).addOnCompleteListener(ProviderChangePasswordActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (user != null) {
                                    update_password(NewPass, ConfirmPass);
                                }
                            } else {
                                oldPass.setError("Password is invalid");
                                oldPass.setText("");
                            }
                        }
                    });
                }

            }
        });
    }

    private void update_password(String NewPass, String ConfirmPass) {


        boolean errorFlag = false;


        if (TextUtils.isEmpty(NewPass)) {
            newPass.setError("Password can not be left empty");
            errorFlag = true;
        }

        if (NewPass.length() < 6) {
            newPass.setError("Password must be more than 6 characters");
            errorFlag = true;
        }

        if (TextUtils.isEmpty(ConfirmPass)) {
            confirmPass.setError("This field can not be left empty");
            errorFlag = true;
        }
        if (!TextUtils.equals(NewPass, ConfirmPass)) {
            newPass.setText("");
            confirmPass.setText("");
            confirmPass.setError("Passwords don't match");
            errorFlag = true;
        }

        if (errorFlag) {
            return;
        } else {
            user.updatePassword(NewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                                    Toast.makeText(ProviderChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProviderChangePasswordActivity.this, PLoginActivity.class));
                                    finish();
                    } else {
                        Toast.makeText(ProviderChangePasswordActivity.this, " Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
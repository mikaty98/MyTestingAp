package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEdit;
    private Button sendBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEdit = findViewById(R.id.emailEdit);
        sendBtn = findViewById(R.id.sendBtn);
        auth = FirebaseAuth.getInstance();

        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String Email = emailEdit.getText().toString().trim();
                boolean errorFlag = false;
                if (TextUtils.isEmpty(Email))
                {
                    emailEdit.setError("Email can not be left empty");
                    errorFlag = true;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    emailEdit.setError("Wrong email format");
                    errorFlag = true;
                }
                if (errorFlag){
                    return;
                }
                auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Password sent to email.", Toast.LENGTH_LONG);
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
    }
}
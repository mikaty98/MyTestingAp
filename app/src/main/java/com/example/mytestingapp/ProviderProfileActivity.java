package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class ProviderProfileActivity extends AppCompatActivity {

    private EditText username,jobDescription,gender,age,id,email,password,phoneNumber;
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        username = findViewById(R.id.username);
        jobDescription = findViewById(R.id.job_description);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phone_number);
        profilePic = findViewById(R.id.profilePic);


        Provider p  = (Provider)getIntent().getParcelableExtra("Provider user");


        username.setText(p.getUserName());
        jobDescription.setText(p.getJobDesc());
        gender.setText(p.getGender());
        age.setText(p.getAge());
        id.setText(p.getId());
        email.setText(p.getEmail());
        password.setText(p.getPassword());
        phoneNumber.setText(p.getPhoneNumber());
        profilePic.setImageBitmap(p.getImageBitmap());


    }
}
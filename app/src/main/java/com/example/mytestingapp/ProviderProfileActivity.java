package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mytestingapp.Classes.Provider;

public class ProviderProfileActivity extends AppCompatActivity {

    private EditText username,jobDescription,gender,birthDate,id,email,phoneNumber;
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        username = findViewById(R.id.username);
        jobDescription = findViewById(R.id.job_description);
        gender = findViewById(R.id.gender);
        birthDate = findViewById(R.id.BirthDate3);
        id = findViewById(R.id.id);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        profilePic = findViewById(R.id.profilePic);


        Provider p  = (Provider)getIntent().getParcelableExtra("Provider user");


        username.setText(p.getUserName());
        jobDescription.setText(p.getJobDesc());
        gender.setText(p.getGender());
        birthDate.setText(p.getBirthDay()+"/"+p.getBirthMonth()+"/"+p.getBirthYear());
        id.setText(p.getId());
        email.setText(p.getEmail());
        phoneNumber.setText(p.getPhoneNumber());
        profilePic.setImageBitmap(p.getImageBitmap());


    }
}
package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SeekerHome extends AppCompatActivity {

    private Button localRequest;
    private Button overboardRequest;
    private Button officialRequest;
    private EditText one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

        Intent intent = getIntent();
        Intent intent1 = getIntent();

        String text = intent1.getStringExtra(SLoginActivity.TEXT);



        localRequest = findViewById(R.id.localService);
        overboardRequest = findViewById(R.id.overboardService);
        officialRequest = findViewById(R.id.officialService);
        one = findViewById(R.id.one);

        one.setError(text);




    }
}
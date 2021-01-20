package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeekerHome extends AppCompatActivity {

    private Button localRequest;
    private Button overboardRequest;
    private Button officialRequest;
    private EditText one, two;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

        Intent intent = getIntent();
        String text = intent.getStringExtra(SLoginActivity.TEXT);





        localRequest = findViewById(R.id.localService);
        overboardRequest = findViewById(R.id.overboardService);
        officialRequest = findViewById(R.id.officialService);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);

        one.setError(text);








    }
}
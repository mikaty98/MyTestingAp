package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SeekerHome extends AppCompatActivity {

    private Button localRequest;
    private Button overboardRequest;
    private Button officialRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home);

        localRequest = findViewById(R.id.localService);
        overboardRequest = findViewById(R.id.overboardService);
        officialRequest = findViewById(R.id.officialService);
    }
}
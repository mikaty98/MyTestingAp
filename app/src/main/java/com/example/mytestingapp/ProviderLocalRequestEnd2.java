package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ProviderLocalRequestEnd2 extends AppCompatActivity {

    Intent intent;

    String receiverId;
    String userType;
    int completionTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_local_request_end2);

        intent = getIntent();


        receiverId = intent.getStringExtra("receiver id");
        completionTime = intent.getIntExtra("completion time", 60);
        userType = intent.getStringExtra("user type");

        Toast.makeText(ProviderLocalRequestEnd2.this,"DONE"+"  "+receiverId+"  "+userType+"   "+completionTime,Toast.LENGTH_LONG).show();

    }
}
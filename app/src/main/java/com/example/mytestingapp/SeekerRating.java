package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SeekerRating extends AppCompatActivity {


    Intent intent;

    String receiverId;
    String userType;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_rating);


        intent = getIntent();

        receiverId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");
        price = intent.getStringExtra("price");

        /*for(int i = 0;i<20;i++)
        {
            Toast.makeText(SeekerRating.this,"DONE"+"  "+receiverId+"   "+userType+ "  "+price,Toast.LENGTH_LONG).show();


        }

         */

    }
}
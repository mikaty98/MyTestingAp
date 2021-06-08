package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class LocalRequestEndBuffer1 extends AppCompatActivity {

    Intent intent, intent1;

    String receiverId, userType, price;
    int  completionTime, flag, priceNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_end_buffer1);


        intent = getIntent();
        receiverId = intent.getStringExtra("receiver id");
        completionTime = intent.getIntExtra("completion time", 60);

        price = intent.getStringExtra("price");
        priceNumber = Integer.valueOf(price);

        userType = intent.getStringExtra("user type");
        flag = intent.getIntExtra("flag", 0);



        SharedPreferences sharedPreferencess = getSharedPreferences("intentt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencess.edit();
        editor.putInt("intentt",flag);
        editor.commit();
        editor.apply();


        intent1 = new Intent(LocalRequestEndBuffer1.this, LocalRequestEnd2.class);
        intent1.putExtra("receiver id", receiverId);
        intent1.putExtra("completion time", completionTime);
        intent1.putExtra("price", price);
        intent1.putExtra("user type", userType);
        startActivity(intent1);


    }
}
package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LocalRequestEndBuffer1 extends AppCompatActivity {

    Intent intent, intent1;

    String receiverId, userType, price;
    int  completionTime, flag, priceNumber;

    String zzz;

    FirebaseUser firebaseUser;



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
        zzz = intent.getStringExtra("zzz");

        Toast.makeText(LocalRequestEndBuffer1.this,"INTENT DONEEEE"+ "   "+flag +" "+zzz,Toast.LENGTH_LONG).show();

       /* SharedPreferences sharedPreferencess = getSharedPreferences("intentt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencess.edit();
        editor.putInt("intentt",flag);
        editor.commit();
        editor.apply();


        SharedPreferences sharedPreferencesss = getSharedPreferences("flag2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferencesss.edit();
        editor1.putString("flag2", zzz);
        editor1.clear();
        editor1.commit();
        editor1.apply();

        */





        intent1 = new Intent(LocalRequestEndBuffer1.this, LocalRequestEnd2.class);
        intent1.putExtra("receiver id", receiverId);
        intent1.putExtra("completion time", completionTime);
        intent1.putExtra("price", priceNumber);
        intent1.putExtra("user type", userType);
        startActivity(intent1);


    }
}
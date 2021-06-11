package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderConfirmationActivity extends AppCompatActivity {

    private Button cancelBtn, acceptBtn;
    private String receiverId, usertype;
    private int arrivalTime,completionTime,price;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_confirmation);

        cancelBtn = findViewById(R.id.cancelBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        receiverId = getIntent().getStringExtra("receiver id");
        usertype = getIntent().getStringExtra("user type");
        arrivalTime = getIntent().getIntExtra("arrival time",0);
        completionTime = getIntent().getIntExtra("completion time",0);
        price = getIntent().getIntExtra("price",20);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                reference.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(true);


                Intent intent = new Intent(ProviderConfirmationActivity.this, ChatRoom.class);
                intent.putExtra("receiver id", receiverId);
                intent.putExtra("user type", usertype);
                intent.putExtra("arrival time", arrivalTime);
                intent.putExtra("completion time", completionTime);
                intent.putExtra("price", price);
                startActivity(intent);

            }
        });
    }
}
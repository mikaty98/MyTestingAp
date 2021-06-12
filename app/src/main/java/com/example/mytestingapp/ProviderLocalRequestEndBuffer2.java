package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderLocalRequestEndBuffer2 extends AppCompatActivity {


    Intent intent;

    String receiverId;
    String userType;
    int completionTime;

    DatabaseReference reference;

    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_local_request_end_buffer2);


        intent = getIntent();

        receiverId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");
        completionTime = intent.getIntExtra("completion time",60);

        reference = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestArrivalConfirm").child(receiverId).child("finalPrice");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                String one = snapshot.getValue(String.class);

                if(one != null)
                {
                    price = one;

                    Intent intent1 = new Intent(ProviderLocalRequestEndBuffer2.this, ProviderLocalRequestEnd2.class);
                    intent1.putExtra("receiver id", receiverId);
                    intent1.putExtra("completion time", completionTime);
                    intent1.putExtra("user type", userType);
                    intent1.putExtra("price", price);

                    startActivity(intent1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
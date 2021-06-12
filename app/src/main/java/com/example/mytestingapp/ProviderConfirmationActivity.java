package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderConfirmationActivity extends AppCompatActivity {

    private Button cancelBtn, acceptBtn;
    private String receiverId, usertype;
    private int arrivalTime,completionTime,price;
    private DatabaseReference reference, reference1;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_confirmation);

        cancelBtn = findViewById(R.id.cancelBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

        String zzz = firebaseUser.getUid();

        String zzzz = zzz;


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                reference1 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");

                reference1.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot child: snapshot.getChildren())
                            {
                                String key = child.getKey();

                                if(key.contains(zzzz))
                                {
                                    receiverId = child.child("seekerID").getValue(String.class);
                                    usertype = child.child("userType").getValue(String.class);
                                    completionTime = child.child("completionTime").getValue(Integer.class);

                                    Intent intent = new Intent(ProviderConfirmationActivity.this, ChatRoom.class);
                                    intent.putExtra("receiver id", receiverId);
                                    intent.putExtra("user type", usertype);
                                    intent.putExtra("completion time", completionTime);

                                    reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                                    reference.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(true);

                                    startActivity(intent);
                                }

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
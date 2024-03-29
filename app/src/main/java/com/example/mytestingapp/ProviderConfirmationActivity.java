package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private int arrivalTime, completionTime, price;
    private DatabaseReference reference, reference1, reference8, reference9, reference10, reference11, reference12, reference13;


    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_confirmation);

        cancelBtn = findViewById(R.id.cancelBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String providerCheckId = firebaseUser.getUid();

                reference1 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");

                reference1.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String key = child.getKey();

                                if (key.contains(providerCheckId)) {
                                    receiverId = child.child("seekerID").getValue(String.class);
                                    usertype = child.child("userType").getValue(String.class);
                                    completionTime = child.child("completionTime").getValue(Integer.class);

                                    reference10 = FirebaseDatabase.getInstance().getReference("Providers");
                                    reference10.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").setValue(false);

                                    reference11 = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(receiverId).child(FirebaseAuth.getInstance().getUid());

                                    reference11.child("deleted").setValue("yes");


                                    reference13 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                                    reference13.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(2);

                                    Intent intent = new Intent(ProviderConfirmationActivity.this, ProviderHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
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

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String providerCheckId = firebaseUser.getUid();

                reference1 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");

                reference1.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String key = child.getKey();

                                if (key.contains(providerCheckId)) {
                                    receiverId = child.child("seekerID").getValue(String.class);
                                    usertype = child.child("userType").getValue(String.class);
                                    completionTime = child.child("completionTime").getValue(Integer.class);
                                    arrivalTime = child.child("arrivalTime").getValue(Integer.class);
                                    price = child.child("price").getValue(Integer.class);



                                    Intent intent = new Intent(ProviderConfirmationActivity.this, ProviderLocalRequestEnd1.class);
                                    intent.putExtra("receiver id", receiverId);
                                    intent.putExtra("user type", usertype);
                                    intent.putExtra("completion time", completionTime);
                                    intent.putExtra("arrival time", arrivalTime);
                                    intent.putExtra("price", price);


                                    reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                                    reference.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(1);//true

                                    startActivity(intent);
                                    finish();

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

    private void goBack() {
        reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(receiverId).child(FirebaseAuth.getInstance().getUid());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference = FirebaseDatabase.getInstance().getReference("Providers");
                reference.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").setValue(false);


                //delete connection between seeker and provider
                reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                reference.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(2);
                //--------------------------------------------------------


                Intent intent = new Intent(ProviderConfirmationActivity.this, ProviderHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String providerCheckId = firebaseUser.getUid();

        reference1 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String key = child.getKey();

                        if (key.contains(providerCheckId)) {
                            receiverId = child.child("seekerID").getValue(String.class);
                            usertype = child.child("userType").getValue(String.class);
                            completionTime = child.child("completionTime").getValue(Integer.class);

                            reference8 = FirebaseDatabase.getInstance().getReference("Providers");
                            reference8.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").setValue(false);

                            reference9 = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(receiverId).child(FirebaseAuth.getInstance().getUid());

                            reference9.child("deleted").setValue("yes");


                            reference12 = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                            reference12.child(receiverId + FirebaseAuth.getInstance().getUid()).child("startFlag").setValue(2);

                            Intent intent = new Intent(ProviderConfirmationActivity.this, ProviderHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
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
    private int arrivalTime,completionTime,price;
    private DatabaseReference reference, reference1;

    private SharedPreferences sp;
    private Provider provider;

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

                goBack();

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

                String providerCheckId = firebaseUser.getUid();

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

                                if(key.contains(providerCheckId))
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

    private void goBack(){
        sp = getApplicationContext().getSharedPreferences("DatasentToPLogin", Context.MODE_PRIVATE);
        String seekerEmail = sp.getString("seeker email","");
        String temp[] = seekerEmail.split(".com");
        reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(temp[0]).child(FirebaseAuth.getInstance().getUid());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference = FirebaseDatabase.getInstance().getReference("Providers");
                reference.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            provider = snapshot.child(FirebaseAuth.getInstance().getUid()).getValue(Provider.class);
                            provider.setSentProposal(false);
                            reference.child(FirebaseAuth.getInstance().getUid()).setValue(provider);

                            //delete connection between seeker and provider
                            reference = FirebaseDatabase.getInstance().getReference().child("StartingConnections");
                            reference.child(receiverId + FirebaseAuth.getInstance().getUid()).removeValue();
                            //--------------------------------------------------------


                            Intent intent = new Intent(ProviderConfirmationActivity.this, ProviderHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
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
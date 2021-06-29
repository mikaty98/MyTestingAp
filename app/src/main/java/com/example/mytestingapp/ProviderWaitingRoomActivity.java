package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderWaitingRoomActivity extends AppCompatActivity {

    private Button cancelBtn;
    private Provider provider;
    private DatabaseReference reference, reference1, reference2, reference3, reference4, reference5, reference6;
    private SharedPreferences sp, sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_waiting_room);

        sp1 = getApplicationContext().getSharedPreferences("DatasentToPLogin", Context.MODE_PRIVATE);
        String seekerId = sp1.getString("seeker id","");



        reference1 = FirebaseDatabase.getInstance().getReference().child("LocalRequests").child(seekerId);


        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                reference2 = FirebaseDatabase.getInstance().getReference("Providers");
                reference2.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child(FirebaseAuth.getInstance().getUid()).child("gotAccepted").getValue(boolean.class) == false
                                && snapshot.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").getValue(boolean.class) == true )
                        {
                            reference5 = FirebaseDatabase.getInstance().getReference("Providers");
                            reference5.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").setValue(false);

                            goBack();
                            Toast.makeText(ProviderWaitingRoomActivity.this,"This request has been deleted",Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference1.removeEventListener(this);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        reference3 = FirebaseDatabase.getInstance().getReference().child("LocalRequestsProposals").child(seekerId);


        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                reference4 = FirebaseDatabase.getInstance().getReference("Providers");
                reference4.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child(FirebaseAuth.getInstance().getUid()).child("gotAccepted").getValue(boolean.class) == false
                                && snapshot.child(FirebaseAuth.getInstance().getUid()).child("sentProposal").getValue(boolean.class) == true )
                        {
                            goBack();

                            reference6 = FirebaseDatabase.getInstance().getReference("LocalRequests").child(seekerId);
                            reference6.child("picked").setValue("yes");


                            Toast.makeText(ProviderWaitingRoomActivity.this,"The service seeker has chosen another provider proposal",Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference3.removeEventListener(this);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cancelBtn = findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cancel Your Proposal")
                        .setMessage("Are you sure you want to cancel your proposal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goBack();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancel Your Proposal")
                .setMessage("Are you sure you want to cancel your proposal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goBack();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void goBack(){
        sp = getApplicationContext().getSharedPreferences("DatasentToPLogin", Context.MODE_PRIVATE);
        String seekerID = sp.getString("seeker id","");
        reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(seekerID).child(FirebaseAuth.getInstance().getUid());
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
                            Intent intent = new Intent(ProviderWaitingRoomActivity.this, ProviderHomeActivity.class);
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
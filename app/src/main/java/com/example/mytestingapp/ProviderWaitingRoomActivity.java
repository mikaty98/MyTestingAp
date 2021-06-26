package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestingapp.Classes.Provider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderWaitingRoomActivity extends AppCompatActivity {

    private Button cancelBtn;
    private Provider provider;
    private DatabaseReference reference;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_waiting_room);

        cancelBtn = findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Canceling Proposal")
                        .setMessage("Are you sure you want to cancel proposal?")
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
                .setTitle("Canceling Proposal")
                .setMessage("Are you sure you want to cancel proposal?")
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
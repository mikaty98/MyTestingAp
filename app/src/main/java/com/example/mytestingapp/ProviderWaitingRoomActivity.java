package com.example.mytestingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderWaitingRoomActivity extends AppCompatActivity {

    private Button cancelBtn;
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
                                String seekerEmail = getIntent().getStringExtra("seeker email");
                                String temp[] = seekerEmail.split(".com");
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(temp[0]).child(FirebaseAuth.getInstance().getUid());
                                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                });
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
                        String seekerEmail = getIntent().getStringExtra("seeker email");
                        String temp[] = seekerEmail.split(".com");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(temp[0]).child(FirebaseAuth.getInstance().getUid());
                        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}
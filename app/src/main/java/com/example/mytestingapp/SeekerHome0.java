package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.chooser.ChooserTargetService;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.SendNotificationPack.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class SeekerHome0 extends AppCompatActivity {

    private Button localService, globalService;
    private Button localWaiting, globalWaiting, signOut;
    private TextView welcomeUser;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home0);

        UpdateToken();


        globalService = findViewById(R.id.globalService);
        globalWaiting = findViewById(R.id.globalWaiting);
        localService = findViewById(R.id.localService);
        localWaiting = findViewById(R.id.localWaiting);
        welcomeUser = findViewById(R.id.welcomeUser);
        signOut = findViewById(R.id.signOut);

        Intent intent = getIntent();
        String text = intent.getStringExtra("seeker userName");

        welcomeUser.setText("Welcome " + text);


        localService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(SeekerHome0.this, "You can't request two local services at the same time!", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerLocalRequest.class);
                            intent.putExtra("seeker email", text);
                            startActivity(intent);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });

        localWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerLocalRequestWaitingList.class);
                            intent.putExtra("seeker email", text);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SeekerHome0.this, "You didn't request a local service yet", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });



        globalService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GlobalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(SeekerHome0.this, "You can't request two global services at the same time!", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerGlobalRequest.class);
                            intent.putExtra("seeker email", text);
                            startActivity(intent);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });



        globalWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("GlobalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerGlobalRequestWaitingList.class);
                            intent.putExtra("seeker email", text);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SeekerHome0.this, "You didn't request a global service yet", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getUid());
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SeekerHome0.this, SLoginActivity.class));
                        finish();
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token userToken = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(userToken);
    }
}
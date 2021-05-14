package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.Seeker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SeekerHome0 extends AppCompatActivity {

    private Button service;
    private Button waiting,signOut;
    private TextView welcomeUser;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_home0);

        service = findViewById(R.id.service);
        waiting = findViewById(R.id.waiting);
        welcomeUser = findViewById(R.id.welcomeUser);
        signOut = findViewById(R.id.signOut);

        Intent intent = getIntent();
        String text = intent.getStringExtra("seeker email");

        welcomeUser.setText("Welcome "+ text);



        service.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){



                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {


                        if(dataSnapshot.exists())
                        {
                            Toast.makeText(SeekerHome0.this, "You can't request two services at the same time!", Toast.LENGTH_SHORT).show();


                        }

                        else
                        {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerHome.class);
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

        waiting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("LocalRequests");
                ref.orderByChild("seekerEmail").equalTo(text).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {


                        if(dataSnapshot.exists())
                        {
                            Toast.makeText(SeekerHome0.this, "Loading...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SeekerHome0.this, SeekerLocalRequestWaitingList.class);
                            intent.putExtra("seeker email", text);
                            startActivity(intent);

                        }

                        else
                        {
                            Toast.makeText(SeekerHome0.this, "You didn't request a service yet", Toast.LENGTH_SHORT).show();

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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SeekerHome0.this,SLoginActivity.class));
                finish();
            }
        });





    }
}
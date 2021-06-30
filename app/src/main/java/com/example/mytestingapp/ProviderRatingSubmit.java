package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.ProviderRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderRatingSubmit extends AppCompatActivity {


    String seekerId;
    String userType;
    String price;
    String providerName;
    Button userBtn;


    Button submitBtn;

    EditText price_value, ratingNote, review;

    RatingBar ratingBar;

    FirebaseUser firebaseUser, firebaseUser1;

    DatabaseReference reference, reference1;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_rating_submit);


        seekerId = getIntent().getStringExtra("receiver id");
        userType = getIntent().getStringExtra("user type");
        price = getIntent().getStringExtra("price");


        ratingNote = findViewById(R.id.ratingNote);
        userBtn = findViewById(R.id.userBtn);
        price_value = findViewById(R.id.price_value);
        review = findViewById(R.id.review);

        submitBtn = findViewById(R.id.submitButton);

        ratingBar = findViewById(R.id.rating_bar);

        price_value.setText("FINAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: " + price);

        ratingNote.setText("RATE THE SEEKER");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String userId = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference("Providers");
                reference.orderByChild("userID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            providerName = snapshot.child(userId).child("userName").getValue(String.class);
                            String one = review.getText().toString().trim();
                            float two = ratingBar.getRating();

                            ProviderRating providerRating = new ProviderRating(seekerId,providerName,one,two);

                            reference = FirebaseDatabase.getInstance().getReference().child("Seekers").child(seekerId).child("ratings");
                            reference.child(userId).setValue(providerRating);

                            reference = FirebaseDatabase.getInstance().getReference("LocalRequests").child(seekerId);
                            reference.removeValue();

                            //Local request proposals removal
                            //reference = FirebaseDatabase.getInstance().getReference("LocalRequestsProposals").child(seekerId);
                            //reference.removeValue();

                            //completion removal
                            reference = FirebaseDatabase.getInstance().getReference("ProviderLocalRequestCompletionConfirm").child(userId);
                            reference.removeValue();

                            //setting busy to false
                            reference = FirebaseDatabase.getInstance().getReference("Providers");
                            reference.child(userId).child("sentProposal").setValue(false);

                            firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

                            String myId = firebaseUser1.getUid();


                            DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference("Providers");

                            mref2.child(myId).child("gotAccepted").setValue(false);

                            Intent intent = new Intent(ProviderRatingSubmit.this, ProviderHomeActivity.class);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run()
                                {
                                    startActivity(intent);
                                    finish();

                                }
                            }, 2000);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });


        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference("Seekers").child(seekerId);
                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String userName = dataSnapshot.child("userName").getValue(String.class);
                        String userEmail = dataSnapshot.child("email").getValue(String.class);

                        String birthDay = dataSnapshot.child("birthDay").getValue(String.class);
                        String birthMonth = dataSnapshot.child("birthMonth").getValue(String.class);
                        String birthYear = dataSnapshot.child("birthYear").getValue(String.class);

                        String birthDate = birthDay+"/"+birthMonth+"/"+birthYear;

                        String IdNumber = dataSnapshot.child("id").getValue(String.class);
                        String userGender = dataSnapshot.child("gender").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(ProviderRatingSubmit.this)
                                            .setTitle("Service Seeker Details")
                                            .setMessage("Name:  "+userName+"\n\n"+ "Email:  "+userEmail+"\n\n"+
                                                    "Gender:  "+userGender+"\n\n" + "Birth Date:  "+birthDate+"\n\n" +
                                                    "Phone Number:  "+phoneNumber+"\n\n" +"ID Number:  "+IdNumber+"\n\n"
                                            )
                                            .setCancelable(false)
                                            .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {

                                                }
                                            }).show();
                                }
                            }
                        });



                        //do what you want with the likes
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


    }
}
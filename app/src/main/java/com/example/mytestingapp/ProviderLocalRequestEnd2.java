package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestingapp.Classes.SeekerLocalRequestCompletionConfirm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ProviderLocalRequestEnd2 extends AppCompatActivity {

    TextView textView;
    EditText editText, note, final_price;
    Chronometer timer;
    Long passedTime;

    Button userBtn;


    private LinearLayout hiddenLayout;


    Button confirmBtn;

    DatabaseReference reference1;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    Intent intent;

    String receiverId;
    String userType;
    int completionTime;

    String price;

    long finalPrice;

    String finalPricee;

    int priceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_local_request_end2);

        setTitle("Completion Time Tracking");

        intent = getIntent();

        receiverId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");
        completionTime = intent.getIntExtra("completion time", 60);
        price = intent.getStringExtra("price");


        priceNumber = Integer.valueOf(price);


        finalPrice = priceNumber;

        userBtn = findViewById(R.id.userBtn);


        textView = findViewById(R.id.text_view2021);
        editText = findViewById(R.id.price_value);
        note = findViewById(R.id.note);
        timer = findViewById(R.id.timer);
        final_price = findViewById(R.id.final_price);
        hiddenLayout = findViewById(R.id.hidden_layout);
        confirmBtn = findViewById(R.id.confirmBtn);

        editText.setText("INITIAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: " + price + " EGP");

        note.setText("NOTE: EACH 5-MINUTE PERIOD PAST THE COMPLETION TIME WILL DEDUCT 1 EGP FROM THE PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER");

        long duration = TimeUnit.MINUTES.toMillis(completionTime);

        CountDownTimer countDownTimer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;


                String yy = String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
                textView.setText(yy);


            }

            @Override
            public void onFinish() {

                textView.setText("00:00");

                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();

            }
        }.start();


        reference1 = FirebaseDatabase.getInstance().getReference().child("SeekerLocalRequestCompletionConfirm").child(receiverId);


        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                hiddenLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootNode = FirebaseDatabase.getInstance();
                SeekerLocalRequestCompletionConfirm s = new SeekerLocalRequestCompletionConfirm(FirebaseAuth.getInstance().getCurrentUser().getUid());
                s.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference = rootNode.getReference().child("ProviderLocalRequestCompletionConfirm");
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(s);


                textView.setText("00:00");

                countDownTimer.cancel();

                timer.stop();

                passedTime = Long.MIN_VALUE;

                passedTime = SystemClock.elapsedRealtime() - timer.getBase();

                long minutes = TimeUnit.MILLISECONDS.toMinutes(passedTime);

                long deductedMoney = minutes / 5;

                finalPrice = finalPrice - deductedMoney;

                finalPricee = String.valueOf(finalPrice);

                final_price.setText("FINAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: " + finalPricee + " EGP");


                Intent intentt = new Intent(ProviderLocalRequestEnd2.this, ProviderRatingSubmit.class);
                intentt.putExtra("receiver id", receiverId);
                intentt.putExtra("price", finalPricee);
                intentt.putExtra("user type", "provider");

                startActivity(intentt);

            }
        });




        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference("Seekers").child(receiverId);
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
                                    new AlertDialog.Builder(ProviderLocalRequestEnd2.this)
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
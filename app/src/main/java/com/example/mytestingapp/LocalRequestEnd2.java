package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
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

public class LocalRequestEnd2 extends AppCompatActivity {

    TextView textView;
    EditText editText, note, final_price;
    Chronometer timer;
    Long passedTime;
    Intent intent;

    String receiverId;

    private ProgressDialog progressDialog;

    private LinearLayout hiddenLayout;


    Button confirmBtn, userBtn1;

    int  completionTime;
    long finalPrice;
    String price;
    String userType;

    int pricee;
    String finalPricee;


    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    DatabaseReference reference1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_end2);


        setTitle("Completion Time Tracking");

        intent = getIntent();
        completionTime = intent.getIntExtra("completion time", 0);
        price = intent.getStringExtra("price");
        receiverId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");

        pricee = Integer.valueOf(price);

        finalPrice = pricee;

        userBtn1 = findViewById(R.id.userBtn1);
        textView = findViewById(R.id.text_view2021);
        editText = findViewById(R.id.price_value);
        note = findViewById(R.id.note);
        timer = findViewById(R.id.timer);
        final_price = findViewById(R.id.final_price);
        hiddenLayout = findViewById(R.id.hidden_layout);
        confirmBtn = findViewById(R.id.confirmBtn);

        editText.setText("Initial Price to be paid by the seeker to the provider: " + price + " EGP");

        note.setText("Note: Each 5 minute-period after the completion time will deduct 1 EGP from the price to be paid by the seeker to the provider");

        if(userType.equals("seeker"))
        {
            hiddenLayout.setVisibility(View.VISIBLE);
        }

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
            public void onFinish()
            {

                textView.setText("00:00");

                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();

            }
        }.start();


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                progressDialog = new ProgressDialog(LocalRequestEnd2.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog2);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



                rootNode = FirebaseDatabase.getInstance();
                SeekerLocalRequestCompletionConfirm s = new SeekerLocalRequestCompletionConfirm(FirebaseAuth.getInstance().getCurrentUser().getUid());
                s.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference = rootNode.getReference().child("SeekerLocalRequestCompletionConfirm");
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(s);


                reference1 = FirebaseDatabase.getInstance().getReference().child("ProviderLocalRequestCompletionConfirm").child(receiverId);


                reference1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                    {
                        textView.setText("00:00");

                        countDownTimer.cancel();

                        timer.stop();

                        passedTime = Long.MIN_VALUE;

                        passedTime = SystemClock.elapsedRealtime() - timer.getBase();

                        long minutes = TimeUnit.MILLISECONDS.toMinutes(passedTime);

                        long deductedMoney = minutes / 5;

                        finalPrice = finalPrice - deductedMoney;

                        finalPricee = String.valueOf(finalPrice);

                        final_price.setText("Final Price to be paid by the seeker to the provider: "+finalPricee+" EGP");


                        Intent intentt = new Intent(LocalRequestEnd2.this, SeekerRatingSubmit.class);
                        intentt.putExtra("receiver id", receiverId);
                        intentt.putExtra("price", finalPricee);
                        intentt.putExtra("user type", "seeker");

                        startActivity(intentt);
                        finish();



                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                    {

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

            }
        });


        userBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference("Providers").child(receiverId);
                mref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String IdNumber = dataSnapshot.child("id").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                        String userEmail = dataSnapshot.child("email").getValue(String.class);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(LocalRequestEnd2.this)
                                            .setTitle("Service Provider Details")
                                            .setMessage("Email:   "+ userEmail+ "\n\n"+ "Phone Number:  "+phoneNumber+"\n\n" +"ID Number:  "+IdNumber+"\n\n")
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
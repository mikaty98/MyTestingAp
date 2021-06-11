package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class LocalRequestEnd2 extends AppCompatActivity {

    TextView textView;
    EditText editText, note, final_price;
    Chronometer timer;
    Long passedTime;
    Intent intent;

    String receiverId;

    private LinearLayout hiddenLayout;


    Button confirmBtn;

    int  completionTime;
    long finalPrice;
    int price;
    String userType;

    String pricee;
    String finalPricee;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_end2);


        setTitle("Completion Time Tracking");

        intent = getIntent();
        completionTime = intent.getIntExtra("completion time", 0);
        price = intent.getIntExtra("price", 0);
        receiverId = intent.getStringExtra("receiver id");
        userType = intent.getStringExtra("user type");

        pricee = Integer.toString(price);

        finalPrice = price;

        textView = findViewById(R.id.text_view2021);
        editText = findViewById(R.id.price_value);
        note = findViewById(R.id.note);
        timer = findViewById(R.id.timer);
        final_price = findViewById(R.id.final_price);
        hiddenLayout = findViewById(R.id.hidden_layout);
        confirmBtn = findViewById(R.id.confirmBtn);

        editText.setText("Initial Price to be paid by the seeker to the provider: " + pricee + " EGP");

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


                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //
                    }
                }, 10000);



            }
        });
    }
}
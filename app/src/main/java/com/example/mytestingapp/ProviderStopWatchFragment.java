package com.example.mytestingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestingapp.Classes.Provider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderStopWatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderStopWatchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference ref;
    private FirebaseAuth auth;

    TextView textView;
    EditText editText, note, final_price;
    Chronometer timer;
    Long passedTime;

    FirebaseUser firebaseUser;
    private DatabaseReference reference3;

    private ProgressDialog progressDialog;




    String zzz, zzzz;

    private LinearLayout hiddenLayout;


    Button seekerBtn;
    String receiverId;

    int arrivalTime, completionTime, price;
    long finalPrice;
    int flag;
    String userType;
    String pricee;
    String finalPricee;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProviderStopWatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderStopWatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderStopWatchFragment newInstance(String param1, String param2) {
        ProviderStopWatchFragment fragment = new ProviderStopWatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_provider_stop_watch, container, false);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();
        auth = FirebaseAuth.getInstance();

        long start = System.currentTimeMillis();

        zzz = "zzz";

        zzzz = "zzz";

        flag = 0;

        ProviderLocalRequestEnd1 providerLocalRequestEnd1 = (ProviderLocalRequestEnd1) getActivity();

        receiverId = providerLocalRequestEnd1.getReceiverId();

        arrivalTime = providerLocalRequestEnd1.getArrivalTime();
        completionTime = providerLocalRequestEnd1.getCompletionTime();
        price = providerLocalRequestEnd1.getPrice();
        userType = providerLocalRequestEnd1.getUserType();

        pricee = Integer.toString(price);

        finalPrice = price;



        textView = view.findViewById(R.id.text_view2021);
        editText = view.findViewById(R.id.price_value);
        note = view.findViewById(R.id.note);
        timer = view.findViewById(R.id.timer);
        final_price = view.findViewById(R.id.final_price);
        hiddenLayout = view.findViewById(R.id.hidden_layout);
        seekerBtn = view.findViewById(R.id.seekerBtn);

        editText.setText("INITIAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: "+pricee+" EGP");

        note.setText("NOTE: EACH 3-MINUTE PERIOD PAST THE ARRIVAL TIME WILL DEDUCT 1 EGP FROM THE PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER");

        long duration = TimeUnit.MINUTES.toMillis(arrivalTime);

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

            public void onFinish() {

                textView.setText("00:00");

                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();


            }
        }.start();


        reference = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestArrivalConfirm").child(receiverId);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {

                textView.setText("00:00");

                countDownTimer.cancel();

                timer.stop();

                long end = System.currentTimeMillis();

                long timeElapsed = end - start;

                long duration1 = TimeUnit.MINUTES.toMillis(arrivalTime);

                if(timeElapsed < duration1)
                {

                    final_price.setText("FINAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: "+pricee+" EGP");


                }


                else if(timeElapsed > duration1)
                {
                    passedTime = Long.MIN_VALUE;

                    passedTime = timeElapsed - duration1;

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(passedTime);

                    long deductedMoney = minutes / 3;

                    finalPrice = finalPrice - deductedMoney;

                    finalPricee = String.valueOf(finalPrice);

                    final_price.setText("FINAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: "+finalPricee+" EGP");

                }


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








        return view;
    }


}
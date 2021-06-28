package com.example.mytestingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestingapp.Classes.LocalRequestApplicant;
import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.Classes.SeekerLocalRequestArrivalConfirm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StopWatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StopWatchFragment extends Fragment {

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

    int arrivalTime, completionTime, price;
    long finalPrice;
    int flag;
    String userType;
    String pricee;
    String finalPricee;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StopWatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StopWatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StopWatchFragment newInstance(String param1, String param2) {
        StopWatchFragment fragment = new StopWatchFragment();
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

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference();
        auth = FirebaseAuth.getInstance();

        zzz = "zzz";

        zzzz = "zzz";

        flag = 0;

        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);

        LocalRequestEnd1 localRequestEnd1 = (LocalRequestEnd1) getActivity();

        arrivalTime = localRequestEnd1.getArrivalTime();
        completionTime = localRequestEnd1.getCompletionTime();
        price = localRequestEnd1.getPrice();
        userType = localRequestEnd1.getUserType();
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

        if(userType.equals("seeker"))
        {
            hiddenLayout.setVisibility(View.VISIBLE);
        }


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


        seekerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog1);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    }
                });

                flag = 1;

                firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

                zzz = firebaseUser.getUid();

                zzzz = zzz;


                rootNode = FirebaseDatabase.getInstance();
                SeekerLocalRequestArrivalConfirm s = new SeekerLocalRequestArrivalConfirm(zzzz);
                s.setUserId(zzzz);
                s.setFlag(0);

                reference = rootNode.getReference().child("SeekerLocalRequestArrivalConfirm");
                reference.child(zzzz).setValue(s);



                SeekerLocalRequestArrivalConfirm seekerLocalRequestArrivalConfirm = new SeekerLocalRequestArrivalConfirm(zzzz);
                reference3 = FirebaseDatabase.getInstance().getReference().child("SeekerLocalRequestArrivalConfirm").child(zzzz);


                reference3.addChildEventListener(new ChildEventListener() {
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

                        passedTime = Long.MIN_VALUE;

                        passedTime = SystemClock.elapsedRealtime() - timer.getBase();


                        long minutes = TimeUnit.MILLISECONDS.toMinutes(passedTime);

                        long deductedMoney = minutes / 3;

                        finalPrice = finalPrice - deductedMoney;

                        finalPricee = String.valueOf(finalPrice);

                        final_price.setText("FINAL PRICE TO BE PAID BY THE SEEKER TO THE PROVIDER: "+finalPricee+" EGP");

                        reference3.child("finalPrice").setValue(finalPricee);

                        Intent intent = new Intent(getContext(), LocalRequestEnd2.class);
                        intent.putExtra("receiver id", localRequestEnd1.getReceiverId());
                        intent.putExtra("completion time", localRequestEnd1.getCompletionTime());
                        intent.putExtra("price", finalPricee);
                        intent.putExtra("user type", localRequestEnd1.getUserType());

                        intent.putExtra("flag", flag);
                        intent.putExtra("zzz", zzzz);

                        progressDialog.dismiss();



                        startActivity(intent);
                        getActivity().finish();


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


        // Inflate the layout for this fragment

        return view;
    }
}
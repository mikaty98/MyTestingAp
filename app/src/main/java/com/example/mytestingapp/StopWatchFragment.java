package com.example.mytestingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

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

    TextView textView;
    EditText editText, note;
    Chronometer timer;


    int arrivalTime, completionTime, price;
    String pricee;

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

        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);

        LocalRequestEnd1 localRequestEnd1 = (LocalRequestEnd1) getActivity();


        arrivalTime = localRequestEnd1.getArrivalTime();
        completionTime = localRequestEnd1.getCompletionTime();
        price = localRequestEnd1.getPrice();
        pricee = Integer.toString(price);

        textView = view.findViewById(R.id.text_view2021);
        editText = view.findViewById(R.id.price_value);
        note = view.findViewById(R.id.note);
        timer = view.findViewById(R.id.timer);

        editText.setText("Price to be paid by the seeker to the provider: "+pricee+" EGP");

        note.setText("Note: Each 3 minute-period after the arrival time will deduct 1 EGP from the price to be paid by the seeker to the provider");


        long duration = TimeUnit.HOURS.toMillis(arrivalTime); //6 hours

        new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;


                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
                textView.setText(yy);


            }

            public void onFinish() {

                textView.setText("00:00:00");

                timer.start();

            }
        }.start();





        // Inflate the layout for this fragment
        return view;
    }
}
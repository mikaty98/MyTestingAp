package com.example.mytestingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.mytestingapp.Classes.FragmentAdapter;
import com.example.mytestingapp.Classes.ProviderFragmentAdapter;
import com.example.mytestingapp.Classes.SeekerLocalRequestArrivalConfirm;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;

public class ProviderLocalRequestEnd1 extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager2 pager2;
    ProviderFragmentAdapter adapter;
    Intent intent;
    String receiverId, userType;
    int arrivalTime, completionTime, price;

    DatabaseReference reference3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_local_request_end1);



        intent = getIntent();
        receiverId = intent.getStringExtra("receiver id");
        arrivalTime = intent.getIntExtra("arrival time", 60);
        completionTime = intent.getIntExtra("completion time", 60);
        price = intent.getIntExtra("price", 0);
        userType = intent.getStringExtra("user type");



        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new ProviderFragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Chat Room"));
        tabLayout.addTab(tabLayout.newTab().setText("Arrival Time Tracking"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });




        SeekerLocalRequestArrivalConfirm seekerLocalRequestArrivalConfirm = new SeekerLocalRequestArrivalConfirm(receiverId);
        reference3 = FirebaseDatabase.getInstance().getReference("SeekerLocalRequestArrivalConfirm").child(receiverId);



        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if (snapshot.exists())
                {
                    //Toast.makeText(ChatRoom.this,"DONEEEEEEEEEEEEEEEEEEEEEE",Toast.LENGTH_LONG).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(ProviderLocalRequestEnd1.this)
                                        .setTitle("Arrival Confirmation")
                                        .setMessage("Please confirm your arrival")
                                        .setCancelable(false)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                reference3.child("flag").setValue(1);

                                                Intent intent = new Intent(ProviderLocalRequestEnd1.this, ProviderLocalRequestEndBuffer2.class);
                                                intent.putExtra("receiver id", receiverId);
                                                intent.putExtra("completion time", completionTime);
                                                intent.putExtra("user type", userType);

                                                dialog.dismiss();

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run()
                                                    {

                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, 4000);


                                            }
                                        }).show();
                            }
                        }
                    });
                }
                else
                {
                    // Toast.makeText(ChatRoom.this,"NOOOOOOOOOOOOOOOOO",Toast.LENGTH_LONG).show();
                }

                reference3.removeEventListener(this);


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




    }

    public String getReceiverId()
    {
        return receiverId;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public int getCompletionTime()
    {
        return completionTime;
    }

    public int getPrice()
    {
        return price;
    }

    public String getUserType()
    {
        return userType;
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage("Cannot leave this page until the transaction is complete.")
                .setPositiveButton("Ok", null)
                .show();
    }

}
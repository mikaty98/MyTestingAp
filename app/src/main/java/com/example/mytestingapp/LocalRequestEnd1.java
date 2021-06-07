package com.example.mytestingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.mytestingapp.Classes.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import java.nio.BufferUnderflowException;

public class LocalRequestEnd1 extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;
    Intent intent;
    String receiverId, userType;
    int arrivalTime, completionTime, price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_request_end1);


        intent = getIntent();
        receiverId = intent.getStringExtra("receiver id");
        arrivalTime = intent.getIntExtra("arrival time", 0);
        completionTime = intent.getIntExtra("completion time", 0);
        price = intent.getIntExtra("price", 0);
        userType = intent.getStringExtra("user type");






        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Alert")
                .setMessage("Cannot leave this page until the transaction is complete.")
                .setPositiveButton("Ok", null)
                .show();
    }

}
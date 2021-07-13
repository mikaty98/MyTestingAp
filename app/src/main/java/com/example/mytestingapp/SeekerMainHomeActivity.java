package com.example.mytestingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.mytestingapp.SendNotificationPack.Token;
import com.example.mytestingapp.ui.FragmentSystem.ProviderSectionsPagerAdapter;
import com.example.mytestingapp.ui.FragmentSystem.SeekerSectionsPageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SeekerMainHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UpdateToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main_home);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String text = getIntent().getStringExtra("seeker userName");

        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem homeTab = findViewById(R.id.homeTab);
        TabItem profileTab = findViewById(R.id.profileTab);
        TabItem settingsTab = findViewById(R.id.settingsTab);
        ViewPager viewPager = findViewById(R.id.viewPager);


        SeekerSectionsPageAdapter seekerSectionsPageAdapter = new
                SeekerSectionsPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),text);

        viewPager.setAdapter(seekerSectionsPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token userToken = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(userToken);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to close the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }



}
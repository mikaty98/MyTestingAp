package com.example.mytestingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Adapter;

import com.example.mytestingapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProviderHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);

        String providerEmail = getIntent().getStringExtra("provider email");

        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem exploreTab = findViewById(R.id.exploreTab);
        TabItem profileTab = findViewById(R.id.profileTab);
        TabItem settingsTab = findViewById(R.id.settingsTab);
        ViewPager viewPager = findViewById(R.id.viewPager);

        SectionsPagerAdapter sectionsPagerAdapter = new
                SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),providerEmail);

        viewPager.setAdapter(sectionsPagerAdapter);

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
}
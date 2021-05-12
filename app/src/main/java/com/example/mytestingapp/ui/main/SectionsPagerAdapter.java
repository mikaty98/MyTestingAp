package com.example.mytestingapp.ui.main;




import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {




    private int numOfTabs;
    private String userID;
    private FragmentManager fragmentManager;




    public SectionsPagerAdapter(FragmentManager fm,int num,String userID) {
        super(fm);
        fragmentManager = fm;
        numOfTabs = num;
        this.userID = userID;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        switch (position){
            case 0:
                ExploreFragment exploreFragment = new ExploreFragment();
                exploreFragment.setArguments(args);
                return exploreFragment;
            case 1: //return new ProfileFragment();
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(args);
                return profileFragment;

            case 2: return new SettingsFragment();
            default: return null;
        }

    }





    @Override
    public int getCount() {

        return numOfTabs;
    }
}
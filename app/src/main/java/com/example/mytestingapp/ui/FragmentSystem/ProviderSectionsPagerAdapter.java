package com.example.mytestingapp.ui.FragmentSystem;




import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ProviderSectionsPagerAdapter extends FragmentPagerAdapter {




    private int numOfTabs;
    private String userID;
    private FragmentManager fragmentManager;




    public ProviderSectionsPagerAdapter(FragmentManager fm, int num, String userID) {
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
                LocalExploreFragment localExploreFragment = new LocalExploreFragment();
                localExploreFragment.setArguments(args);
                return localExploreFragment;
            case 1:
                GlobalExploreFragment globalExploreFragment = new GlobalExploreFragment();
                globalExploreFragment.setArguments(args);
                return globalExploreFragment;
            case 2: //return new ProfileFragment();
                ProviderProfileFragment profileFragment = new ProviderProfileFragment();
                profileFragment.setArguments(args);
                return profileFragment;

            case 3: return new ProviderSettingsFragment();
            default: return null;
        }

    }





    @Override
    public int getCount() {

        return numOfTabs;
    }
}
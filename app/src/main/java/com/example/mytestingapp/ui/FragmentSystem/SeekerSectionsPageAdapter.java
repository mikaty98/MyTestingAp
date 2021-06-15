package com.example.mytestingapp.ui.FragmentSystem;



import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SeekerSectionsPageAdapter extends FragmentPagerAdapter {


    private int numOfTabs;
    private String seekerUserName;
    private FragmentManager fm;

    public SeekerSectionsPageAdapter(FragmentManager fm, int numOfTabs, String seekerUserName) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.seekerUserName = seekerUserName;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        switch (position){
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                args.putString("seeker userName", seekerUserName);
                homeFragment.setArguments(args);
                return homeFragment;
            case 1: //return new ProfileFragment();
                SeekerProfileFragment profileFragment = new SeekerProfileFragment();
                profileFragment.setArguments(args);
                return profileFragment;

            case 2: return new SeekerSettingsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

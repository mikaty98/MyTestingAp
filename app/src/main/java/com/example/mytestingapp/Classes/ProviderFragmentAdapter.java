package com.example.mytestingapp.Classes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mytestingapp.ChatRoomFragment;
import com.example.mytestingapp.ProviderChatRoomFragment;
import com.example.mytestingapp.ProviderStopWatchFragment;
import com.example.mytestingapp.StopWatchFragment;

public class ProviderFragmentAdapter extends FragmentStateAdapter {
    public ProviderFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        if(position == 0)
        {

            ProviderChatRoomFragment providerChatRoomFragment = new ProviderChatRoomFragment();
            return providerChatRoomFragment;
        }

        if(position == 1)
        {
            ProviderStopWatchFragment providerStopWatchFragment = new ProviderStopWatchFragment();
            return providerStopWatchFragment;
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
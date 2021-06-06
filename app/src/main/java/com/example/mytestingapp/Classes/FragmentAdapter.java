package com.example.mytestingapp.Classes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mytestingapp.ChatRoomFragment;
import com.example.mytestingapp.StopWatchFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        if(position == 0)
        {

            ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
            return chatRoomFragment;
        }

        if(position == 1)
        {
            StopWatchFragment stopWatchFragment = new StopWatchFragment();
            return stopWatchFragment;
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

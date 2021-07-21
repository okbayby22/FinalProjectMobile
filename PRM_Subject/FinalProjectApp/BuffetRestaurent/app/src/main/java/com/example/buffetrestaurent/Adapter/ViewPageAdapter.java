package com.example.buffetrestaurent.Adapter;



import androidx.annotation.NonNull;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

public class ViewPageAdapter extends FragmentStateAdapter {
    private int numOfTabs;

    public ViewPageAdapter(FragmentActivity fm){
        super(fm);

    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}

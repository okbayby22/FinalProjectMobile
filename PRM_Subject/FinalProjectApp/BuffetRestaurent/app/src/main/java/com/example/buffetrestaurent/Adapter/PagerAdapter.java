package com.example.buffetrestaurent.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.buffetrestaurent.Controller.Fragment.signInFragment;
import com.example.buffetrestaurent.Controller.Fragment.signUpFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private int numOfTabs;

    public PagerAdapter(FragmentActivity fm){
        super(fm);

    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new signInFragment();
            case 1:
                return new signUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

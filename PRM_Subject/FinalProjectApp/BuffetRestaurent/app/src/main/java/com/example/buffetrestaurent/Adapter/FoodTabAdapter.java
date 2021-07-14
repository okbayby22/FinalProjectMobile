package com.example.buffetrestaurent.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.buffetrestaurent.Controller.Fragment.foodListHomePageFragment;

public class FoodTabAdapter extends FragmentStateAdapter {
    private int numOfTabs;

    public FoodTabAdapter(FragmentActivity fm){
        super(fm);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new foodListHomePageFragment(1);
            case 1:
                return new foodListHomePageFragment(2);
            case 2:
                return new foodListHomePageFragment(3);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

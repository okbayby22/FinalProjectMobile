package com.example.buffetrestaurent.Adapter;



import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.buffetrestaurent.Controller.Fragment.tableFragement;

public class ViewPageAdapter extends FragmentStateAdapter {
    private int numOfTabs;

    public ViewPageAdapter(FragmentActivity fm){
        super(fm);

    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new tableFragement(1);
            case 1:
                return new tableFragement(2);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

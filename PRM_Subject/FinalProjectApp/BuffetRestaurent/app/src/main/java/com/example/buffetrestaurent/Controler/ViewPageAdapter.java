package com.example.buffetrestaurent.Controler;



import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class ViewPageAdapter extends FragmentStateAdapter {
    private int numOfTabs;

    public ViewPageAdapter(FragmentActivity fm){
        super(fm);

    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new tableFragement();
            case 1:
                return new tableFragement();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

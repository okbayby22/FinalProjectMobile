package com.example.buffetrestaurent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;
import android.widget.TextView;

import com.example.buffetrestaurent.Controler.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomePageStaff extends AppCompatActivity {
    TabLayout homepagetabTabLayout;
    ViewPager2 homepageViewPager;
    TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_staff);
        homepagetabTabLayout = findViewById(R.id.tabLayout);
        homepageViewPager = findViewById(R.id.viewPage);
        userName = findViewById(R.id.userName);
        userName.setText(getIntent().getStringExtra("emailUser"));
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        homepageViewPager.setAdapter(viewPageAdapter);


    }
}
package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Bundle;
import android.widget.TextView;

import com.example.buffetrestaurent.Controler.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomePageStaff extends AppCompatActivity {
    TabLayout homepagetabTabLayout;
    ViewPager2 homepageViewPager;
    TextView userName;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_staff);
        homepagetabTabLayout = findViewById(R.id.tabLayout);
        homepageViewPager = findViewById(R.id.viewPage);
        userName = findViewById(R.id.userName);
        //userName.setText(getIntent().getStringExtra("emailUser"));

        userEmail= getIntent().getStringExtra("USER_EMAIL");

        userName.setText(userEmail);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        homepageViewPager.setAdapter(viewPageAdapter);

        new TabLayoutMediator(homepagetabTabLayout, homepageViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==0){
                    tab.setText("LIVE TABLE");
                }
                else if(position == 1){
                    tab.setText("EMPTY TABLE");
                }
            }
        }).attach();


    }
}
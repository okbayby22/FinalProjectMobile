package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.buffetrestaurent.Controler.PagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout mainTabLayout;
    ViewPager2 tabView;
    PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTabLayout=findViewById(R.id.tabBar);
        tabView=findViewById(R.id.viewPage);

        getSupportActionBar().hide();

        pagerAdapter=new PagerAdapter(this
                );
        tabView.setAdapter(pagerAdapter);

        new TabLayoutMediator(mainTabLayout, tabView, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==0){
                    tab.setText(R.string.strSignIn);
                }
                else{
                    tab.setText(R.string.strSignUp);
                }
            }
        }).attach();
    }
}
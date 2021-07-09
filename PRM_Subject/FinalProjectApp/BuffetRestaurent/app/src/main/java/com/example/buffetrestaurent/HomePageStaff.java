package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.buffetrestaurent.Controler.UserChangePassword;
import com.example.buffetrestaurent.Controler.UserProfile;
import com.example.buffetrestaurent.Controler.ViewPageAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomePageStaff extends AppCompatActivity {
    TabLayout homepagetabTabLayout;
    ViewPager2 homepageViewPager;
    TextView userName;
    String userEmail;
    Toolbar homepageToolBar;
    DrawerLayout homepageDrawer;
    NavigationView homepageNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_staff);
        homepagetabTabLayout = findViewById(R.id.tabLayout);
        homepageViewPager = findViewById(R.id.viewPage);
        //userName = findViewById(R.id.userName);
        homepageDrawer=findViewById(R.id.homepagestaff_drawView);
        homepageNavigationView =findViewById(R.id.homepagestaff_navView);
        homepageToolBar = findViewById(R.id.Homepagestaff_toolbar);
        setSupportActionBar(homepageToolBar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,homepageDrawer,homepageToolBar,R.string.strOpenMenu,R.string.strCloseMenu);
        homepageDrawer.addDrawerListener(toggle);
        toggle.syncState();
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        homepageNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.staffmenu_btnprofile:
                        intent=new Intent(HomePageStaff.this, UserProfile.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        break;
                    case R.id.staffmenu_btnReserHis:
                        break;
                    case R.id.staffmenu_btnMyDiscount:
                        break;
                    case R.id.staffmenu_btnChangePass:
                        intent=new Intent(HomePageStaff.this, UserChangePassword.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        break;
                    case R.id.staffmenu_btnMenu:
                        intent=new Intent(HomePageStaff.this, MenuMangeForStaff.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        break;
                }
                homepageDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        homepageNavigationView.bringToFront();
        //userName.setText(userEmail);
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
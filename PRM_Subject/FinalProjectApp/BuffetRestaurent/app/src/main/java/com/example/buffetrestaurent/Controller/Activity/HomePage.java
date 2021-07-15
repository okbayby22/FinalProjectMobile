package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.buffetrestaurent.Adapter.FoodTabAdapter;
import com.example.buffetrestaurent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout homepageTabLayout;
    ViewPager2 tabView;
    FoodTabAdapter foodTabAdapter;
    BottomNavigationView bottomNavigationView;
    DrawerLayout homepageDrawer;
    NavigationView homepageNavigationView;
    Toolbar homepageToolBar;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        homepageTabLayout=findViewById(R.id.homepage_FoodTabBar);
        tabView=findViewById(R.id.homepage_ViewPage);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        homepageDrawer=findViewById(R.id.homepage_drawView);
        homepageNavigationView=findViewById(R.id.homepage_navView);
        homepageToolBar=findViewById(R.id.toolbar);

        userEmail= getIntent().getStringExtra("USER_EMAIL");

        setSupportActionBar(homepageToolBar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,homepageDrawer,homepageToolBar,R.string.strOpenMenu,R.string.strCloseMenu);
        homepageDrawer.addDrawerListener(toggle);
        toggle.syncState();

        homepageNavigationView.setNavigationItemSelectedListener(this);
        homepageNavigationView.bringToFront();
        //getSupportActionBar().hide();

        foodTabAdapter=new FoodTabAdapter(this);
        tabView.setAdapter(foodTabAdapter);

        new TabLayoutMediator(homepageTabLayout, tabView, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==0){
                    tab.setText(R.string.strFood);
                }
                else if(position == 1){
                    tab.setText(R.string.strDrink);
                }
                else{
                    tab.setText(R.string.strDessert);
                }
            }
        }).attach();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.homepage_menu:

                        return true;
                    case R.id.discount_menu:
                        intent=new Intent(HomePage.this, CustomerBuyDiscountActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.add_menu:
                        intent = new Intent(HomePage.this,AddReservation.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.viewmenu_menu:
                        intent = new Intent(HomePage.this, CustomerMenu.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.history_menu:
                        intent=new Intent(HomePage.this, CancelReservation.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(homepageDrawer.isDrawerOpen(GravityCompat.START)){
            homepageDrawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainmenu_btnprofile:
                Intent intent=new Intent(this, UserProfile.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
                break;
            case R.id.mainmenu_btnReserHis:
                break;
            case R.id.mainmenu_btnMyDiscount:
                intent=new Intent(this,CustomerDiscountHistoryActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
                break;
            case R.id.mainmenu_btnChangePass:
                intent=new Intent(this, UserChangePassword.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
                break;
        }
        homepageDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
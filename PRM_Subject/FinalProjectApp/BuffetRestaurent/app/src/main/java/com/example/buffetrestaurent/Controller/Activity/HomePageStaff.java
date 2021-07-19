package com.example.buffetrestaurent.Controller.Activity;

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
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.ViewPageAdapter;
import com.example.buffetrestaurent.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class HomePageStaff extends AppCompatActivity {
    TabLayout homepagetabTabLayout;
    ViewPager2 homepageViewPager;
    TextView userName;
    String userEmail;
    Toolbar homepageToolBar;
    DrawerLayout homepageDrawer;
    NavigationView homepageNavigationView;
    private long pressedTime;
    double staffRole;

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
        getSupportActionBar().setTitle("Buffet Restaurant");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,homepageDrawer,homepageToolBar,R.string.strOpenMenu,R.string.strCloseMenu);
        homepageDrawer.addDrawerListener(toggle);
        toggle.syncState();

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        staffRole = getIntent().getDoubleExtra("ROLE",1);

        homepageNavigationView.getMenu().clear();
        if(staffRole==2){
            homepageNavigationView.inflateMenu(R.menu.manager_menu);
        }
        else if(staffRole==1){
            homepageNavigationView.inflateMenu(R.menu.staff_menu);
        }


        homepageNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.staffmenu_btnprofile:
                        intent=new Intent(HomePageStaff.this, StaffProfile.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        intent.putExtra("INTENT",2);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnBalance:
                        intent=new Intent(HomePageStaff.this, AddBalanceActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnMyDiscount:
                        intent=new Intent(HomePageStaff.this, StaffDiscountManagement.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnChangePass:
                        intent=new Intent(HomePageStaff.this, StaffChangePasswordActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnMenu:
                        intent=new Intent(HomePageStaff.this, MenuMangeForStaff.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnStaffManage:
                        intent=new Intent(HomePageStaff.this, StaffManageActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnConfirmReservation:
                        intent=new Intent(HomePageStaff.this, ConfirmReservation.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnManageFood:
                        intent=new Intent(HomePageStaff.this, StaffManageFoodActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnCustomerManage:
                        intent=new Intent(HomePageStaff.this, UserManageActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.staffmenu_btnSignOut:
                        intent=new Intent(HomePageStaff.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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

    @Override
    public void onBackPressed() {
        if(homepageDrawer.isDrawerOpen(GravityCompat.START)){
            homepageDrawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to sign out", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }
}
package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.ViewPageAdapter;
import com.example.buffetrestaurent.Controller.Fragment.discountStaffManagement;
import com.example.buffetrestaurent.Controller.Fragment.expandTextCustomer;
import com.example.buffetrestaurent.Controller.Fragment.expandTextDiscount;
import com.example.buffetrestaurent.Controller.Fragment.expandableText;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomePageStaff extends AppCompatActivity {

    //contain user name of signing user
    TextView userName;
    //contain user email of signing user
    String userEmail;
    Toolbar homepageToolBar;
    DrawerLayout homepageDrawer;
    NavigationView homepageNavigationView;
    private long pressedTime;
    //contain staff role
    double staffRole;
    //title of statistic information
    TextView titleReservation,titleDisocunt,titleCustomer;
    //count time of click title
    int countClick=1,countClick1=1,countClick2=1;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_staff);
        /*
        find view by ID for all component in need
         */
        homepageDrawer=findViewById(R.id.homepagestaff_drawView);
        homepageNavigationView =findViewById(R.id.homepagestaff_navView);
        homepageToolBar = findViewById(R.id.Homepagestaff_toolbar);
        userName = findViewById(R.id.homepagestaff_nameStaff);
        /*
        create action bar
         */
        setSupportActionBar(homepageToolBar);
        getSupportActionBar().setTitle("Buffet Restaurant");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,homepageDrawer,homepageToolBar,R.string.strOpenMenu,R.string.strCloseMenu);
        homepageDrawer.addDrawerListener(toggle);
        toggle.syncState();

        /*
        get intent from sender
         */
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        loadData();
        //set staff name for saying hi to user
        setStaffName();
        //get staff role
        staffRole = getIntent().getDoubleExtra("ROLE",1);
        /*
        create homepage navigation depend on role
         */
        homepageNavigationView.getMenu().clear();
        if(staffRole==2){
            homepageNavigationView.inflateMenu(R.menu.manager_menu);
        }
        else if(staffRole==1){
            homepageNavigationView.inflateMenu(R.menu.staff_menu);
        }
        /*
        event click for homepage navigation
         */
        homepageNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    /*
                     Pressing profile to start intent to staff profile
                     */
                    case R.id.staffmenu_btnprofile:
                        intent=new Intent(HomePageStaff.this, StaffProfile.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        intent.putExtra("ID", ID);
                       intent.putExtra("INTENT",2);
                        startActivity(intent);
                        finish();
                        break;
                    /*
                    Pressing Balance to start intent to add balance activity
                     */
                    case R.id.staffmenu_btnBalance:
                        intent=new Intent(HomePageStaff.this, AddBalanceActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    /*
                    Pressing Discount to go to staff discount management
                     */
                    case R.id.staffmenu_btnMyDiscount:
                        intent=new Intent(HomePageStaff.this, StaffDiscountManagement.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                    /*
                    Pressing Change pass to start intent to change password of that staff
                     */
                    case R.id.staffmenu_btnChangePass:
                        intent=new Intent(HomePageStaff.this, StaffChangePasswordActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing Menu to start intent menu management
                         */
                    case R.id.staffmenu_btnMenu:
                        intent=new Intent(HomePageStaff.this, MenuMangeForStaff.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing Staff manage to start intent to Staff Management
                         */
                    case R.id.staffmenu_btnStaffManage:
                        intent=new Intent(HomePageStaff.this, StaffManageActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing Confirm Reservation to start intent to Confirm Reservation
                         */
                    case R.id.staffmenu_btnConfirmReservation:
                        intent=new Intent(HomePageStaff.this, ConfirmReservation.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing Manage Food to start intent to food management
                         */
                    case R.id.staffmenu_btnManageFood:
                        intent=new Intent(HomePageStaff.this, StaffManageFoodActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing customer management to start intent to customer management
                         */
                    case R.id.staffmenu_btnCustomerManage:
                        intent=new Intent(HomePageStaff.this, UserManageActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", staffRole);
                        startActivity(intent);
                        finish();
                        break;
                        /*
                        Pressing signout to sign out from application
                         */
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
        //bring navigation to front
        homepageNavigationView.bringToFront();
        /*
        Find view by ID for component in need
         */
        titleReservation = findViewById(R.id.homepagestaff_titleReservation);
        titleDisocunt =findViewById(R.id.homepagestaff_titleDiscount);
        titleCustomer = findViewById(R.id.homepagestaff_titleCustomer);
        /*
        event for clicking statistic titles
         */
        titleReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++countClick; //Increase count click
                //Check whether count click is odd or event
                if(countClick%2==0){
                    /*
                     replace fragment onto frame layout
                     */
                    //Get support fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //create fragment transaction
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.homepagestaff_framelayout,new expandableText(),"reservation");
                    //Commit
                    fragmentTransaction.commit();
                }
                else{
                    /*
                    Remove fragment by tag that creating form replace phase
                     */
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment f = fm.findFragmentByTag("reservation");
                    removeFragement(f);
                }
            }
        });
        titleDisocunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++countClick1;//Increase count click
                //Check whether count click is odd or event
                if(countClick1%2==0){
                     /*
                     replace fragment onto frame layout
                     */
                    //Get support fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //create fragment transaction
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.homepagestaff_framelayoutDiscount,new expandTextDiscount(),"discount");
                    //Commit
                    fragmentTransaction.commit();
                }
                else{
                     /*
                    Remove fragment by tag that creating form replace phase
                     */
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment f = fm.findFragmentByTag("discount");
                    removeFragement(f);
                }
            }
        });
        titleCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++countClick2;//Increase count click
                //Check whether count click is odd or event
                if(countClick2%2==0){
                    /*
                     replace fragment onto frame layout
                     */
                    //Get support fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //create fragment transaction
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.homepagestaff_framelayoutCustomer,new expandTextCustomer(),"customer");
                    //Commit
                    fragmentTransaction.commit();
                }
                else{
                     /*
                    Remove fragment by tag that creating form replace phase
                     */
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment f = fm.findFragmentByTag("customer");
                    removeFragement(f);
                }
            }
        });

    }

    /**
     * remove frament which is send to this function
     * @param fragment
     */
    public void removeFragement(Fragment fragment){
        //Get support fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //create fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //remove fragment
        fragmentTransaction.remove(fragment).commit();
    }

    /**
     * overid onBackPress event
     */
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
    /**
     * load all staff and store name in userName
     */
    public void setStaffName(){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("staffs")
                    .whereEqualTo("staffEmail",userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            QuerySnapshot query = task.getResult();
                            //check query whether is null
                            if(query.isEmpty()){
                            }else{
                                //Store staff that get from document
                                Staff staff = new Staff();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //get staff
                                    staff = document.toObject(Staff.class);
                                }
                                //store user name and creating hello sentence
                                userName.setText("Hello, "+staff.getStaffName());
                            }
                        }
                    });
    }
    private void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("staffEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ID=document.getString("staffId");
                            }
                        }
                    }
                });
    }
}
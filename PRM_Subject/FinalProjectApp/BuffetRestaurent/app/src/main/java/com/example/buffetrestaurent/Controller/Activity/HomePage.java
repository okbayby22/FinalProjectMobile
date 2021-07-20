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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.buffetrestaurent.Adapter.FoodTabAdapter;
import com.example.buffetrestaurent.Model.Menu;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout homepageTabLayout; //Tab layout to show all food in store
    ViewPager2 tabView; //contain fragment load food
    FoodTabAdapter foodTabAdapter; //Adapter of food fragment
    BottomNavigationView bottomNavigationView; //Bottom nav menu
    DrawerLayout homepageDrawer; // Side nav menu layout
    NavigationView homepageNavigationView; // Side nav menu
    Toolbar homepageToolBar; // toolbar
    private long pressedTime; // Save user click backpress time
    TextView txtCusEmail,txtCusBalance;
    ImageView cusAvatar;

    String userEmail; // Save user email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /*
        Get view by id
         */
        homepageTabLayout=findViewById(R.id.homepage_FoodTabBar);
        tabView=findViewById(R.id.homepage_ViewPage);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        homepageDrawer=findViewById(R.id.homepage_drawView);
        homepageNavigationView=findViewById(R.id.homepage_navView);
        homepageToolBar=findViewById(R.id.toolbar);
        View headerView = homepageNavigationView.getHeaderView(0);
        txtCusBalance=headerView.findViewById(R.id.homepage_txtCusBalance);
        txtCusEmail=headerView.findViewById(R.id.homepage_txtCusEmail);
        cusAvatar=headerView.findViewById(R.id.homepage_customerAvatar);


        //Get user email
        userEmail= getIntent().getStringExtra("USER_EMAIL");

        /*
        Set action bar to custom action bar
         */
        setSupportActionBar(homepageToolBar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,homepageDrawer,homepageToolBar,R.string.strOpenMenu,R.string.strCloseMenu);
        homepageDrawer.addDrawerListener(toggle);
        toggle.syncState();

        /*
        Set event listener for side nav menu
         */
        homepageNavigationView.setNavigationItemSelectedListener(this);
        homepageNavigationView.bringToFront();

        foodTabAdapter=new FoodTabAdapter(this);
        tabView.setAdapter(foodTabAdapter);

        /*
        Set title for food tab layout
         */
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

        /*
        Set event for bottom nav menu
         */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            /**
             * Method intent to new activity base on customerclick in bottom nav menu
             * @param item Menu item
             * @return
             */
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
        loadData();
    }

    /**
     * Method for phone back button. If user click back in home page 2 time between 2 second user will sign out
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
     * Method intent to new activity base on user click in side nav menu
     * @param item menu item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainmenu_btnprofile:
                Intent intent=new Intent(this, UserProfile.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
                break;
            case R.id.mainmenu_btnMyDiscount:
                intent=new Intent(this,CustomerDiscountHistoryActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("IntentID","From_Home");
                startActivity(intent);
                finish();
                break;
            case R.id.mainmenu_btnChangePass:
                intent=new Intent(this, UserChangePassword.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
                break;
            case R.id.mainmenu_btnSignOut:
                intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        homepageDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method load data to side nav menu
     */
    private void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .whereEqualTo("customerEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                txtCusEmail.setText("Hello, "+document.getString("customerName"));
                                double balance=document.getDouble("customerBalance");
                                txtCusBalance.setText("Balance: "+balance+" VND");
                                Picasso.get().load(document.getString("customerAvatar")).into(cusAvatar);
                            }
                        }
                    }
                });
    }
}
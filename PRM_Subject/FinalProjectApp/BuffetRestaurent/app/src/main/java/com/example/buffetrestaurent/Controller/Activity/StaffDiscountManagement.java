package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.buffetrestaurent.Controller.Fragment.addDiscountStaffManagement;
import com.example.buffetrestaurent.Controller.Fragment.discountStaffManagement;
import com.example.buffetrestaurent.R;

public class StaffDiscountManagement extends AppCompatActivity {
    //Button component
    Button btnADD, btnList;
    //Contain email of using user
    String userEmail;
    double staffRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_discount_management);
        btnADD = findViewById(R.id.discountStaffManagement_ButtonAdd);
        btnList = findViewById(R.id.discountStaffManagement_ButtonBack);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strDiscountManage);
        //Get intent from sender
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        staffRole = getIntent().getDoubleExtra("ROLE",0);

        //Disable button list when it first start
        btnList.setEnabled(false);
        //set default is fragement that display list of discount
        replaceFragement(new discountStaffManagement(userEmail,staffRole));
        //create event click fo add button
        btnADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enable click list button to return list discount
                btnList.setEnabled(true);
                //disable button add
                btnADD.setEnabled(false);
                //call to fragment add discount
                replaceFragement(new addDiscountStaffManagement(userEmail));
            }
        });
        //create event of click on list button
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable list button
                btnList.setEnabled(false);
                //enable add button
                btnADD.setEnabled(true);
                //call to fragment list discount
                replaceFragement(new discountStaffManagement(userEmail,staffRole));
            }
        });
    }

    /**
     * replace fragemnt
     * @param fragment
     */
    private void replaceFragement(Fragment fragment){
        //Get support fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //create fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discountStaffManagement_Framelayout,fragment);
        //Commit
        fragmentTransaction.commit();
    }

    @Override
    /*
    Back button on supported bar
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", staffRole);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", staffRole);
        startActivity(intent);
        this.finish();
    }
}
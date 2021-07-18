package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.buffetrestaurent.Adapter.DeskAdapter;
import com.example.buffetrestaurent.Adapter.DiscountAdapter;
import com.example.buffetrestaurent.Controller.Fragment.addDiscountStaffManagement;
import com.example.buffetrestaurent.Controller.Fragment.discountStaffManagement;
import com.example.buffetrestaurent.Model.Desk;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StaffDiscountManagement extends AppCompatActivity {
    //Button component
    Button btnADD, btnList;
    //Contain email of using user
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_discount_management);
        btnADD = findViewById(R.id.discountStaffManagement_ButtonAdd);
        btnList = findViewById(R.id.discountStaffManagement_ButtonBack);
        //Get intent from sender
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        //Disable button list when it first start
        btnList.setEnabled(false);
        //set default is fragement that display list of discount
        replaceFragement(new discountStaffManagement());
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
                replaceFragement(new discountStaffManagement(userEmail));
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

}
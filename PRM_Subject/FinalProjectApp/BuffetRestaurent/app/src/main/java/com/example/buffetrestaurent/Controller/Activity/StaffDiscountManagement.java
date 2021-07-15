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
    Button btnADD, btnBack;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_discount_management);
        btnADD = findViewById(R.id.discountStaffManagement_ButtonAdd);
        btnBack = findViewById(R.id.discountStaffManagement_ButtonBack);
        userEmail= getIntent().getStringExtra("USER_EMAIL");

        btnBack.setEnabled(false);
        replaceFragement(new discountStaffManagement());
        btnADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setEnabled(true);
                btnADD.setEnabled(false);
                replaceFragement(new addDiscountStaffManagement(userEmail));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setEnabled(false);
                btnADD.setEnabled(true);
                replaceFragement(new discountStaffManagement());
            }
        });
    }
    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discountStaffManagement_Framelayout,fragment);
        fragmentTransaction.commit();
    }

}
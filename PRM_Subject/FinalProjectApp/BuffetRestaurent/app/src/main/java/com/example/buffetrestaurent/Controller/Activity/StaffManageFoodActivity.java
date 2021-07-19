package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buffetrestaurent.Adapter.StaffManageFoodAdapter;
import com.example.buffetrestaurent.Adapter.StaffMenuFoodAdapter;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.Model.Menu;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StaffManageFoodActivity extends AppCompatActivity {

    String userEmail;
    ArrayList<Food> listFood;
    StaffManageFoodAdapter fAdapter;
    EditText txtSearch;
    FloatingActionButton btnAdd;
    double staffRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_food);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strFoodManage);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        staffRole = getIntent().getDoubleExtra("ROLE",0);

        listFood=new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.staffManageFood_txtLView);
        txtSearch=findViewById(R.id.staffManageFood_txtSearch);
        btnAdd=findViewById(R.id.staffManageFood_btnAdd);

        fAdapter = new StaffManageFoodAdapter(listFood,userEmail,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(txtSearch.getText().toString());
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , AddFoodActivity.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                finish();
            }
        });

        loadData();
    }

    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Food food = document.toObject(Food.class);
                                listFood.add(food);
                            }
                            fAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filter(String s) {
        ArrayList<Food> newlist = new ArrayList<>();

        for (Food item : listFood) {
            if (String.valueOf(item.getFoodName()).toLowerCase().contains(s.toLowerCase())) {
                newlist.add(item);
            }
        }
        fAdapter.ArrayFilter(newlist);
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
package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Adapter.StaffManageAdapter;
import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaffManageActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    StaffManageAdapter staffAdap;

    public static ArrayList<Staff> list;

    int AllPosition;

    String email;

    TextView search;

    private void loadStaff() {
        email = getIntent().getStringExtra("USER_EMAIL");
        list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Staff staff = doc.toObject(Staff.class);
                                list.add(staff);
                            }
                            staffAdap = new StaffManageAdapter(list, StaffManageActivity.this); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(StaffManageActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(staffAdap);
                            recyclerView.setLayoutManager(manager);
                        }

                    }
                });

    }


    private void filter(String s) {
        ArrayList<Staff> newlist = new ArrayList<>();

        for (Staff item : list) {
            if (String.valueOf(item.getStaffName()).toLowerCase().contains(s.toLowerCase())) {
                newlist.add(item);
            }
        }
        System.out.println(newlist.size());
        staffAdap.ArrayFilter(newlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage);
        recyclerView = findViewById(R.id.StaffManageActivity_recycler);
        loadStaff();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strStaffManage);
        email = getIntent().getStringExtra("USER_EMAIL");
        search = findViewById(R.id.StaffManageActivity_txtSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(search.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }
}
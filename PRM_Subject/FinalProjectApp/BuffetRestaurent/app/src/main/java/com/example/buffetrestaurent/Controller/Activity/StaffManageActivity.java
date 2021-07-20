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
import android.view.View;
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


    RecyclerView recyclerView; //Recycler to store list of staff

    StaffManageAdapter staffAdap; //Staff adapter of recycler view

    public static ArrayList<Staff> list; //List of staff

    int AllPosition;

    String email; //Email of current user

    TextView search; //Search staff

    double role;

    /**
     * Method use to load list of staff to recycler view
     */
    private void loadStaff() {
        email = getIntent().getStringExtra("USER_EMAIL");
        role = getIntent().getDoubleExtra("ROLE",0);
        list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get list of staff
         */
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
                            /*
                            Binding data to recycler view
                             */
                            staffAdap = new StaffManageAdapter(list, StaffManageActivity.this,role); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(StaffManageActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(staffAdap);
                            recyclerView.setLayoutManager(manager);
                        }

                    }
                });

    }


    /*
    Filter to search name of staff
     */
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
        recyclerView = findViewById(R.id.StaffManageActivity_recycler); //Mapping recycler view to layout
        loadStaff(); //load list of staff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strStaffManage);
        email = getIntent().getStringExtra("USER_EMAIL"); //Get email of current user
        role = getIntent().getDoubleExtra("ROLE",0);
        search = findViewById(R.id.StaffManageActivity_txtSearch); //Mapping search text input to layout

        /*
        Set event of search input when user input
         */
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(search.getText().toString()); //Set search list to recycler view
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", email);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }

    /**
     * Event of Onclick for Button
     * @param view
     */
    public void onClickAddStaff(View view){
        Intent intent = new Intent(this , AddStaffActivity.class );
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }
}
package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.buffetrestaurent.Adapter.CustomerAdapter;
import com.example.buffetrestaurent.Adapter.StaffManageAdapter;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserManageActivity extends AppCompatActivity {


    RecyclerView recyclerView; //Recycler view to store list of customer

    CustomerAdapter cusAdapt; //Customer adapter of recyclerview

    public static ArrayList<Customer> list; //List of customers

    String email; //Email of current user

    TextView search; //Search input for search by customer's name

    double role;


    /**
     * Load list of customer to recycler view
     */
    private void loadCustomer() {
        email = getIntent().getStringExtra("USER_EMAIL");
        list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get list of customer
         */
        db.collection("customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Customer cus = doc.toObject(Customer.class);
                                list.add(cus);
                            }
                            /*
                            Biding data to recycler view
                             */
                            cusAdapt = new CustomerAdapter(list, UserManageActivity.this); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(UserManageActivity.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(cusAdapt);
                            recyclerView.setLayoutManager(manager);
                        }

                    }
                });

    }

    /**
     * Search user by name
     * @param s
     */
    private void filter(String s) {
        ArrayList<Customer> newlist = new ArrayList<>();

        for (Customer item : list) {
            if (String.valueOf(item.getCustomerName()).toLowerCase().contains(s.toLowerCase())) {
                newlist.add(item);
            }
        }
        System.out.println(newlist.size());
        cusAdapt.ArrayFilter(newlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        recyclerView = findViewById(R.id.UserManageActivity_recycler); //Mapping recyclerview to layout
        loadCustomer(); //load list of customer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strCustomerManage);
        email = getIntent().getStringExtra("USER_EMAIL"); //Get email of current user
        role = getIntent().getDoubleExtra("ROLE",0);
        search = findViewById(R.id.UserManageActivity_txtSearch); //Mapping search input to layout
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

    /**
     * Event of Onclick for Button
     * @param item
     */
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

    /**
     * Event of back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }
}
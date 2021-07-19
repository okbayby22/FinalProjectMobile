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
import android.widget.EditText;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmReservation extends AppCompatActivity {

    RecyclerView recyclerView; //Recyclerview store list of reservations of all customer

    ReservationAdapter reserAdap; //Reservation adapter of recyclerview

    public static ArrayList<Reservation> listAll; //List of reservations of all customers

    String email; //Email of current user

    double role;

    /**
     * Load list of reservations
     */
    private void loadReservation() {
        listAll = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get list reservations of all customers
         */
        db.collection("reservations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Reservation res = document.toObject(Reservation.class);
                                res.setReservationId(document.getId());
                                listAll.add(res);
                            }
                            /*
                            Binding data to recyclerview
                             */
                            reserAdap = new ReservationAdapter(listAll, ConfirmReservation.this); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(ConfirmReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(reserAdap);
                            recyclerView.setLayoutManager(manager);
                        } else {

                        }
                    }
                });
    }

    /**
     * Search reservation by customer name
     * @param s
     */
    private void filter(String s) {
        ArrayList<Customer> cusList = new ArrayList<>();
        ArrayList<Reservation> newlist = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer cus = document.toObject(Customer.class);
                                cusList.add(cus);
                            }
                            for (Customer cus : cusList) {
                                if (cus.getCustomerName().toLowerCase().contains(s)) {
                                    for (Reservation item : listAll) {
                                        if (String.valueOf(item.getCustomerId()).contains(cus.getCustomerId())) {
                                            newlist.add(item);
                                        }
                                    }
                                }
                            }
                            System.out.println(newlist.size());
                            reserAdap.ArrayFilter(newlist);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);
        EditText search = findViewById(R.id.ConfirmReservation_txtSearch); //Mapping search input to layout
        email = getIntent().getStringExtra("USER_EMAIL"); //Get email of current user
        role = getIntent().getDoubleExtra("ROLE",0);
        /*
        Set event of search input when user input
         */
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString()); //Set search list to recycler view
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.ConfirmReservation_recycler); //Get the recycler View by ID
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Reservation");
        loadReservation();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    /*
                    Swipe Left to cancel reservation
                     */
                    case ItemTouchHelper.LEFT:
                        Reservation res = listAll.get(viewHolder.getAdapterPosition());
                        /*
                        Can not delete Successful or cancelled reservation
                         */
                        if (res.getReservationStatus() != 0) {
                            new AlertDialog.Builder(ConfirmReservation.this).setTitle("Cancel Reservation Notice")
                                    .setMessage("Can not cancel this reservation")
                                    .show();
                            reserAdap.notifyDataSetChanged();
                        } else {
                            /*
                            Ask for delete reservation
                             */
                            new AlertDialog.Builder(ConfirmReservation.this)
                                    .setTitle("Cancel Reservation Notice")
                                    .setMessage("Do you want to cancel this reservation?")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /*
                                            Cancel reservation and add staff id to reservation
                                             */
                                            db.collection("staffs")
                                                    .whereEqualTo("staffEmail", email)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                DocumentSnapshot staffDoc = task.getResult().getDocuments().get(0);
                                                                Staff staff = staffDoc.toObject(Staff.class);
                                                                Map<String, Object> updateData = new HashMap<>();
                                                                updateData.put("reservationStatus", 2);
                                                                updateData.put("staffId", staff.getStaffId());
                                                                db.collection("reservations")
                                                                        .document(listAll.get(viewHolder.getAdapterPosition()).getReservationId())
                                                                        .update(updateData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    /*
                                                                                    Refund balance to customer
                                                                                     */
                                                                                    db.collection("customers")
                                                                                            .whereEqualTo("customerId", res.getCustomerId())
                                                                                            .get()
                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                                                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                                                        DocumentSnapshot cusDoc = task.getResult().getDocuments().get(0);
                                                                                                        Customer cus = cusDoc.toObject(Customer.class);
                                                                                                        double refund = res.getReservationAmount() + cus.getCustomerBalance();
                                                                                                        Map<String, Object> Data = new HashMap<>();
                                                                                                        Data.put("customerBalance", refund);
                                                                                                        db.collection("customers")
                                                                                                                .document(cus.getCustomerId())
                                                                                                                .update(Data)
                                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            new AlertDialog.Builder(ConfirmReservation.this).setTitle("Refund Notice")
                                                                                                                                    .setMessage("Cancel Reservation Successful, Your Balance Has Been Refund")
                                                                                                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                                            res.setReservationStatus(2);
                                                                                                                                            reserAdap.notifyDataSetChanged();
                                                                                                                                        }
                                                                                                                                    }).show();
                                                                                                                        }
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() { //User press cancel
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reserAdap.notifyDataSetChanged();
                                }
                            }).show();
                        }
                        break;
                        /*
                        Swipe right to confirm reservation
                         */
                    case ItemTouchHelper.RIGHT:
                        res = listAll.get(viewHolder.getAdapterPosition());
                        /*
                        Can not confirm successful or cancelled reservation
                         */
                        if (res.getReservationStatus() != 0) {
                            new AlertDialog.Builder(ConfirmReservation.this).setTitle("Confirm Reservation Notice")
                                    .setMessage("Can not confirm this reservation")
                                    .show();
                            reserAdap.notifyDataSetChanged();
                        } else {
                            /*
                            Ask for confirm reservation
                             */
                            new AlertDialog.Builder(ConfirmReservation.this)
                                    .setTitle("Confirm Reservation Notice")
                                    .setMessage("Do you want to Confirm this reservation?")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() { //User press OK
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            /*
                                            Add staff ID to reservation
                                             */
                                            db.collection("staffs")
                                                    .whereEqualTo("staffEmail", email)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                System.out.println(">>>>>>>>>>>>>>>>>>>>>Toi Day 1");
                                                                DocumentSnapshot staffDoc = task.getResult().getDocuments().get(0);
                                                                Staff staff = staffDoc.toObject(Staff.class);
                                                                Map<String, Object> updateData = new HashMap<>();
                                                                updateData.put("reservationStatus", 1);
                                                                updateData.put("staffId", staff.getStaffId());
                                                                db.collection("reservations")
                                                                        .document(listAll.get(viewHolder.getAdapterPosition()).getReservationId())
                                                                        .update(updateData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    res.setReservationStatus(1);
                                                                                    reserAdap.notifyDataSetChanged();
                                                                                    new AlertDialog.Builder(ConfirmReservation.this).setTitle("Confirm Reservation Notice")
                                                                                            .setMessage("Confirm Reservation Successfully").show();
                                                                                    updatePointCustomer(res.getReservationId());
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() { //User press Cancel
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reserAdap.notifyDataSetChanged();
                                }
                            }).show();
                        }
                        break;
                }


            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * Update point after confirm reservation for customer
     * @param resId
     */
    public void updatePointCustomer(String resId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservations")
                .whereEqualTo("reservationId", resId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            Reservation res = doc.toObject(Reservation.class);
                            db.collection("customers")
                                    .whereEqualTo("customerId", res.getCustomerId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                                Customer cus = doc.toObject(Customer.class);
                                                int point = cus.getCustomerPoint() + (res.getNumberTickets() * 20);
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("customerPoint", point);
                                                db.collection("customers")
                                                        .document(cus.getCustomerId())
                                                        .update(data)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                            }
                                                        });
                                            }

                                        }
                                    });
                        }

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
}


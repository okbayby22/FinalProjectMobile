package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmReservation extends AppCompatActivity {

    RecyclerView recyclerView;

    ReservationAdapter reserAdap;

    public static ArrayList<Reservation> listAll;


    CollectionReference colRef;

    int AllPosition;

    String email;

    private void loadReservation() {
        listAll = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservations")
                .whereEqualTo("reservationStatus", 0)
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
                            reserAdap = new ReservationAdapter(listAll, ConfirmReservation.this); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(ConfirmReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(reserAdap);
                            recyclerView.setLayoutManager(manager);
                        } else {

                        }
                    }
                });
    }

//    private void readData(FireStoreCallBack fireStoreCallBack) {
//
//        colRef.whereEqualTo("reservationStatus",0).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                System.out.println(">>>>>>>>>>>>> Toi day");
//                                Reservation res = document.toObject(Reservation.class);
//                                listAll.add(res);
//                            }
//                            fireStoreCallBack.onCallback(listAll);
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//
//                    }
//                });
//    }


    private interface FireStoreCallBack {
        void onCallback(ArrayList<Reservation> list);
    }

    public void showData() {
        reserAdap = new ReservationAdapter(listAll, ConfirmReservation.this); //Call LecturerAdapter to set data set and show data
        LinearLayoutManager manager = new LinearLayoutManager(ConfirmReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
        recyclerView.setAdapter(reserAdap);
        recyclerView.setLayoutManager(manager);
    }

    private void filter(String s) {
        ArrayList<Reservation> newlist = new ArrayList<>();

        for (Reservation item : listAll) {
            if (String.valueOf(item.getCustomerId()).toLowerCase().contains(s.toLowerCase())) {
                newlist.add(item);
            }
        }
        System.out.println(newlist.size());
        reserAdap.ArrayFilter(newlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);
        EditText search = findViewById(R.id.ConfirmReservation_txtSearch);
        email = getIntent().getStringExtra("USER_EMAIL");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.ConfirmReservation_recycler); //Get the recycler View by ID
        System.out.println(">>>>>>>>>>>>>>>>>>>>>Here");
        getSupportActionBar().hide();
        loadReservation();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        Reservation res = listAll.get(viewHolder.getAdapterPosition());
                        if (res.getReservationStatus() != 0) {
                            new AlertDialog.Builder(ConfirmReservation.this).setTitle("Cancel Reservation Notice")
                                    .setMessage("Can not cancel this reservation")
                                    .show();
                            reserAdap.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(ConfirmReservation.this)
                                    .setTitle("Cancel Reservation Notice")
                                    .setMessage("Do you want to cancel this reservation?")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                                                db.collection("reservation")
                                                                        .document(res.getReservationId())
                                                                        .update(updateData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
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
                                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reserAdap.notifyDataSetChanged();
                                }
                            }).show();
                        }
                        break;
                    case ItemTouchHelper.RIGHT:
                        res = listAll.get(viewHolder.getAdapterPosition());
                        if (res.getReservationStatus() != 0) {
                            new AlertDialog.Builder(ConfirmReservation.this).setTitle("Confirm Reservation Notice")
                                    .setMessage("Can not confirm this reservation")
                                    .show();
                            reserAdap.notifyDataSetChanged();
                        } else {
                            new AlertDialog.Builder(ConfirmReservation.this)
                                    .setTitle("Confirm Reservation Notice")
                                    .setMessage("Do you want to Confirm this reservation?")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                                                db.collection("reservation")
                                                                        .document(res.getReservationId())
                                                                        .update(updateData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    res.setReservationStatus(2);
                                                                                    reserAdap.notifyDataSetChanged();
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                        }
                                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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
}


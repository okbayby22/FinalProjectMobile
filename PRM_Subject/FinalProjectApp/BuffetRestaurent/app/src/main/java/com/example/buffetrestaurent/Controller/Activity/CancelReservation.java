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
import android.view.MenuItem;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Reservation;
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

public class CancelReservation extends AppCompatActivity {


    RecyclerView recyclerView; //Recyclerview store reservations list of current customer

    ReservationAdapter reserAdap; //Reservation adapter of recyclerview

    public static ArrayList<Reservation> list; //List of reservation of current customer

    String email; //Email of current user

    /**
     * Load list of Reservation
     */
    private void loadReservation() {
        email = getIntent().getStringExtra("USER_EMAIL");
        list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get list reservations of current customer
         */
        db.collection("customers")
                .whereEqualTo("customerEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            String docID = doc.getId();
                            db.collection("reservations")
                                    .whereEqualTo("customerId", docID)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Reservation res = document.toObject(Reservation.class);
                                                    res.setReservationId(document.getId());
                                                    list.add(res);
                                                }
                                                /*
                                                Binding data to recyclerview
                                                 */
                                                reserAdap = new ReservationAdapter(list, CancelReservation.this); //Call LecturerAdapter to set data set and show data
                                                LinearLayoutManager manager = new LinearLayoutManager(CancelReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
                                                recyclerView.setAdapter(reserAdap);
                                                recyclerView.setLayoutManager(manager);
                                            } else {

                                            }
                                        }
                                    });
                        }

                    }
                });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        recyclerView = findViewById(R.id.recycler); //Get the recycler View by ID
        loadReservation(); //Call method to load Lecturer Information to list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strViewRes);
        /*
        Swipe left to cancel reservation and refund balance to customer
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Reservation res = list.get(viewHolder.getAdapterPosition());
                /*
                Can not delete successful or cancelled reseravtion
                 */
                if (res.getReservationStatus() != 0) {
                    new AlertDialog.Builder(CancelReservation.this).setTitle("Delete Reservation Notice").setMessage("You can't delete this reservation").show();
                    reserAdap.notifyDataSetChanged();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    /*
                    Ask for delete reservation
                     */
                    new AlertDialog.Builder(CancelReservation.this).setTitle("Delete Reservation Notice").setMessage("Confirm Cancel Reservation")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() { //User press OK then cancel reservation and refund balance
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, Object> updateData = new HashMap<>();
                                    updateData.put("reservationStatus", 2);
                                    /*
                                    Set status of reservation to 2 (cancel)
                                     */
                                    db.collection("reservations")
                                            .document(list.get(viewHolder.getAdapterPosition()).getReservationId())
                                            .update(updateData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    Reservation reservation = list.get(viewHolder.getAdapterPosition());
                                                    /*
                                                    Refund balance to customer
                                                     */
                                                    db.collection("customers")
                                                            .whereEqualTo("customerEmail", email)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                                                        Customer cus = doc.toObject(Customer.class);
                                                                        double refund = reservation.getReservationAmount() + cus.getCustomerBalance();
                                                                        Map<String, Object> Data = new HashMap<>();
                                                                        Data.put("customerBalance", refund);
                                                                        db.collection("customers")
                                                                                .document(cus.getCustomerId())
                                                                                .update(Data)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            new AlertDialog.Builder(CancelReservation.this).setTitle("Refund Notice")
                                                                                                    .setMessage("Cancel Reservation Successful, Your Balance Has Been Refund") //Annouce that balance has been refund to customer
                                                                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                                                        /*
                                                                                                        Update reservation list
                                                                                                         */
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
                                            });
                                }
                            }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() { //if user click cancel
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reserAdap.notifyDataSetChanged();
                        }
                    }).show();
                }

            }
        }).attachToRecyclerView(recyclerView);

    }

    /**
     * Method of button OnClick event
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, HomePage.class);
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }
}
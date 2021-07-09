package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmReservation extends AppCompatActivity {

    RecyclerView recyclerView;

    ReservationAdapter reserAdap;

    public static ArrayList<Reservation> listAll;


    CollectionReference colRef;

    int AllPosition;

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
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("reservationStatus", 2);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("reservations")
                                .document(listAll.get(viewHolder.getAdapterPosition()).getReservationId())
                                .update(updateData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        listAll.remove(viewHolder.getAdapterPosition());
                                        new AlertDialog.Builder(ConfirmReservation.this).setTitle("Delete Reservation Notice").setMessage("Delete Reservation Successfully").show();
                                        reserAdap.notifyDataSetChanged();
                                    }
                                });
                        break;
                    case ItemTouchHelper.RIGHT:
                        Map<String, Object> confirm = new HashMap<>();
                        confirm.put("reservationStatus", 1);
                        FirebaseFirestore fb = FirebaseFirestore.getInstance();
                        fb.collection("reservations")
                                .document(listAll.get(viewHolder.getAdapterPosition()).getReservationId())
                                .update(confirm)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        listAll.remove(viewHolder.getAdapterPosition());
                                        new AlertDialog.Builder(ConfirmReservation.this).setTitle("Confirm Reservation Notice").setMessage("Confirm Reservation Successfully").show();
                                        reserAdap.notifyDataSetChanged();
                                    }
                                });
                }


            }
        }).attachToRecyclerView(recyclerView);


    }
}


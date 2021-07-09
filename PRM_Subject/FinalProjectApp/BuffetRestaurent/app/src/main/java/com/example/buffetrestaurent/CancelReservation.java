package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CancelReservation extends AppCompatActivity {


    RecyclerView recyclerView;

    ReservationAdapter reserAdap;

    public static ArrayList<Reservation> list;

    int AllPosition;

    private void loadReservation() {
        list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservations")
                .whereEqualTo("reservationStatus",0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Reservation res = document.toObject(Reservation.class);
                                String date = String.valueOf(res.getReservationDate());
                                SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date fmtdate =fmt.parse(date);
                                    System.out.println(fmtdate.toString());
                                    res.setReservationDate(fmtdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                list.add(res);
                            }
                            reserAdap = new ReservationAdapter(list, CancelReservation.this); //Call LecturerAdapter to set data set and show data
                            LinearLayoutManager manager = new LinearLayoutManager(CancelReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
                            recyclerView.setAdapter(reserAdap);
                            recyclerView.setLayoutManager(manager);
                        } else{

                        }
                    }
                });
    }

    public void showData() {
        reserAdap = new ReservationAdapter(list, CancelReservation.this); //Call LecturerAdapter to set data set and show data
        LinearLayoutManager manager = new LinearLayoutManager(CancelReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
        recyclerView.setAdapter(reserAdap);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        recyclerView = findViewById(R.id.recycler); //Get the recycler View by ID
        loadReservation(); //Call method to load Lecturer Information to list
        getSupportActionBar().hide();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
                list.remove(viewHolder.getAdapterPosition());
                new AlertDialog.Builder(CancelReservation.this).setTitle("Delete Status").setMessage("Delete Successfully").show();
            }
        }).attachToRecyclerView(recyclerView);

    }
}
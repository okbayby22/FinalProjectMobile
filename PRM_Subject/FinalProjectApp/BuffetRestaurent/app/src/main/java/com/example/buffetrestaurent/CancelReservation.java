package com.example.buffetrestaurent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Reservation;

import java.sql.Date;
import java.util.ArrayList;

public class CancelReservation extends AppCompatActivity {


    RecyclerView recyclerView;

    ReservationAdapter reserAdap;

    public static ArrayList<Reservation> list;

    int AllPosition;

    private ArrayList<Reservation> loadReservation() {
        list = new ArrayList<>();
        list.add(new Reservation(Date.valueOf("2021-06-17"),"15:00",0,3,600000.00,1,1,1,1));
        return list;
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
        list = loadReservation(); //Call method to load Lecturer Information to list
        showData();
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
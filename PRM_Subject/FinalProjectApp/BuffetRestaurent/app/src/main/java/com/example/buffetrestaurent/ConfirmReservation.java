package com.example.buffetrestaurent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.buffetrestaurent.Adapter.ReservationAdapter;
import com.example.buffetrestaurent.Model.Reservation;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;

public class ConfirmReservation extends AppCompatActivity {

    RecyclerView recyclerView;

    ReservationAdapter reserAdap;

    public static ArrayList<Reservation> list;

    int AllPosition;

    private ArrayList<Reservation> loadReservation() {
        list = new ArrayList<>();
        list.add(new Reservation(Date.valueOf("2021-06-17"),"15:00",0,3,600000.00,1,1,1,1));
        list.add(new Reservation(Date.valueOf("2021-06-18"),"15:00",0,3,700000.00,1,2,1,1));
        list.add(new Reservation(Date.valueOf("2021-06-19"),"14:00",0,3,700000.00,1,2,1,1));
        list.add(new Reservation(Date.valueOf("2021-06-20"),"15:00",0,3,700000.00,1,1,1,1));
        return list;
    }

    public void showData() {
        reserAdap = new ReservationAdapter(list, ConfirmReservation.this); //Call LecturerAdapter to set data set and show data
        LinearLayoutManager manager = new LinearLayoutManager(ConfirmReservation.this); //Linear Layout Manager use to handling layout for each Lecturer
        recyclerView.setAdapter(reserAdap);
        recyclerView.setLayoutManager(manager);
    }

    private void filter(String s){
        ArrayList<Reservation> newlist = new ArrayList<>();

        for (Reservation item : list){
            if(String.valueOf(item.getCustomerId()).toLowerCase().contains(s.toLowerCase())){
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
        list = loadReservation(); //Call method to load Lecturer Information to list
        showData();
        getSupportActionBar().hide();
    }
}


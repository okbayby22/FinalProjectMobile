package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateReservation extends AppCompatActivity {

    int numsOftickets;
    Button plus;
    Button minus;
    Button update;
    TextView timepick;
    TextView tickets;
    TextView price;
    EditText name;
    EditText phone;
    CalendarView calendarView;
    DecimalFormat vnd = new DecimalFormat("###,###");
    String date;
    public static ArrayList<Reservation> list;
    ReservationAdapter reserAdap;
    String reservationId;
    String email;
    int hour,minute;
    private void loadInformation(){
        email = getIntent().getStringExtra("USER_EMAIL");
        reservationId =getIntent().getStringExtra("GETID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .whereEqualTo("customerEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer customerInfor = document.toObject(Customer.class);
                                name.setText(customerInfor.getCustomerName());
                                phone.setText(customerInfor.getCustomerPhone());
                            }
                        } else {

                        }
                    }
                });
        Reservation inforReser = (Reservation) getIntent().getSerializableExtra("GETRESERVATION");
        tickets.setText(inforReser.getNumberTickets()+"");
        timepick.setText(inforReser.getReservationTime());
        try {
            calendarView.setDate(new SimpleDateFormat("MM/dd/yyyy").parse(inforReser.getReservationDate()).getTime(), true, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        numsOftickets = inforReser.getNumberTickets();
        price.setText(vnd.format(numsOftickets * 200000) + " VND");
        String timehour[] = inforReser.getReservationTime().split(":");
        hour = Integer.parseInt(timehour[0]);
        minute = Integer.parseInt(timehour[1]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);
        getSupportActionBar().setTitle(R.string.strUpdateRes);
        timepick = findViewById(R.id.UpdateReservation_txtTimePick);           //Text to show time
        plus = findViewById(R.id.UpdateReservation_btnIncrease);               //Plus button to increase number of tickets
        minus = findViewById(R.id.UpdateReservation_btnDecrease);              //Minus button to increase number of tickets
        update = findViewById(R.id.UpdateReservation_btnUpdate);            //Add button to add new reservation
        tickets = findViewById(R.id.UpdateReservation_txtNumTickets);          //Tickets textview to show number of tickets
        price = findViewById(R.id.UpdateReservation_txtPrice);                 //Price textview to show total balance that user must pay
        numsOftickets = Integer.parseInt(tickets.getText().toString());     //Variable to save data of number of tickets
        name = findViewById(R.id.UpdateReservation_inputName);                 //Input text field phone
        phone = findViewById(R.id.UpdateReservation_inputPhone);               //Input text field name
        calendarView = findViewById(R.id.UpdateReservation_CalendarView);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date curdate =  Calendar.getInstance().getTime();
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        name.setClickable(false);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        phone.setClickable(false);
        date = sdf.format(curdate);
        loadInformation();
        /*
        Set Event on Date Change on Calendar View
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = (month + 1) + "/" + dayOfMonth + "/" + year;
            }
        });
        name.setHint("Enter your name");
        phone.setHint("Enter your phone number");
        /*
        Set Event on Click on Button Add
         */
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reservationid = 3;
                int deskid = 0;
                int numogticket = Integer.parseInt(tickets.getText().toString());
                Double amount = new Double(numsOftickets * 200000);
                int status = 0;
                String time = timepick.getText().toString();
                int cusid = 1;
                int discountid = 1;
                int staffid = 0;
                /*
                If Customer does not pick time
                 */
                if (timepick.getText().toString().equals("Touch Here To Pick Time")) {
                    new AlertDialog.Builder(UpdateReservation.this).setTitle("Pick Time").setMessage("Please pick a time").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("customers")
                            .whereEqualTo("customerEmail", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                        String docID = doc.getId();
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("reservationDate", date);
                                        user.put("reservationTime", timepick.getText().toString());
                                        user.put("reservationStatus", status);
                                        user.put("discountId", "");
                                        db.collection("reservations")
                                                .document(reservationId)
                                                .update(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        Toast.makeText(UpdateReservation.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(UpdateReservation.this, CancelReservation.class);
                                                        i.putExtra("USER_EMAIL", email);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
        /*
        Append time picker dialog when click on textview
         */
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timepicker = new TimePickerDialog(UpdateReservation.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timepick.setText(String.format("%02d:%02d",hourOfDay,minute));
                    }
                }, hour, minute, false);
                timepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timepicker.setTitle("Pick Time");
                timepicker.show();
            }
        });
    }
}
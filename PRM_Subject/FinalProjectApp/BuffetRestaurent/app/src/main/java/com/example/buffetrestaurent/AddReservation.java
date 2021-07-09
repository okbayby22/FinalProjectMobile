package com.example.buffetrestaurent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.ReservationService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReservation extends AppCompatActivity {
    int numsOftickets;
    Button plus;
    Button minus;
    Button add;
    TextView timepick;
    TextView tickets;
    TextView price;
    EditText name;
    EditText phone;
    ReservationService service;
    CalendarView calendarView;
    DecimalFormat vnd = new DecimalFormat("###,###");
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        getSupportActionBar().hide();
        timepick = findViewById(R.id.AddReservation_txtTimePick);           //Text to show time
        plus = findViewById(R.id.AddReservation_btnIncrease);               //Plus button to increase number of tickets
        minus = findViewById(R.id.AddReservation_btnDecrease);              //Minus button to increase number of tickets
        add = findViewById(R.id.AddReservation_btnAdd);                     //Add button to add new reservation
        tickets = findViewById(R.id.AddReservation_txtNumTickets);          //Tickets textview to show number of tickets
        price = findViewById(R.id.AddReservation_txtPrice);                 //Price textview to show total balance that user must pay
        numsOftickets = Integer.parseInt(tickets.getText().toString());     //Variable to save data of number of tickets
        name = findViewById(R.id.AddReservation_inputName);                 //Input text field phone
        phone = findViewById(R.id.AddReservation_inputPhone);               //Input text field name
        calendarView = findViewById(R.id.AddReservation_CalendarView);      //Calendar view
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date curdate =  Calendar.getInstance().getTime();
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        name.setClickable(false);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        phone.setClickable(false);
        date = sdf.format(curdate);
        /*
        Set Event on Date Change on Calendar View
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println(dayOfMonth);
                date = (month+1) + "/" + dayOfMonth + "/" + year;
                System.out.println(date);
            }
        });
        name.setHint("Enter your name");
        phone.setHint("Enter your phone number");
        price.setText(vnd.format(numsOftickets * 200000) + " VND");
        /*
        Set Event on Click on Button Add
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deskid = 0;
                int numogticket = Integer.parseInt(tickets.getText().toString());
                Double amount = new Double(numsOftickets * 200000);
                int status = 0;
                String time = timepick.getText().toString();
                int cusid = 1;
                int discountid = 1;
                int staffid = 0;
                System.out.println(".................."+numogticket);
                /*
                If Customer does not pick time
                 */
                if (timepick.getText().toString().equals("Touch Here To Pick Time")) {
                    new AlertDialog.Builder(AddReservation.this).setTitle("Pick Time").setMessage("Please pick a time").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("reservationId", 4);
                    try {
                        user.put("reservationDate", new SimpleDateFormat("MM/dd/yyyy").parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    user.put("reservationTime", timepick.getText().toString());
                    user.put("reservationStatus", status);
                    user.put("numberTickets", numogticket);
                    user.put("reservationAmount", amount);
                    user.put("deskId", 0);
                    user.put("customerId", 1);
                    user.put("discountId", 1);
                    user.put("staffId", 1);

                    db.collection("reservations")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(v.getContext(),CancelReservation.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("AC", "Error adding document", e);
                                }
                            });
                }
            }
        });
        /*
        Increase number of tickets
         */
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numsOftickets += 1;
                tickets.setText(numsOftickets + "");
                price.setText(vnd.format(numsOftickets * 200000) + " VND");
            }
        });
        /*
        Decrease number of tickets
         */
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numsOftickets -= 1;
                if (numsOftickets <= 0) {
                    numsOftickets = 0;
                }
                tickets.setText(numsOftickets + "");
                price.setText(vnd.format(numsOftickets * 200000) + " VND");
            }
        });
        /*
        Append time picker dialog when click on textview
         */
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timepicker = new TimePickerDialog(AddReservation.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
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
package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.ReservationService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    ReservationService service;
    CalendarView calendarView;
    DecimalFormat vnd = new DecimalFormat("###,###");
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reservation);
        getSupportActionBar().hide();
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
                date = year + "-" + month + "-" + dayOfMonth;
            }
        });
        name.setHint("Enter your name");
        phone.setHint("Enter your phone number");
        price.setText(vnd.format(numsOftickets * 200000) + " VND");
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
//                    Reservation reservation = new Reservation(null, time, status, numogticket, amount, deskid, cusid, discountid, staffid);
//                    service = Apis.getReservationService();
//                    System.out.println("..............."+date);
//                    Call<Reservation> call = service.updateReservation(reservationid, date,reservation);
//                    call.enqueue(new Callback<Reservation>() {
//                        @Override
//                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
//                            if (response.isSuccessful()) {
//                                Toast.makeText(v.getContext(), "Update successful !", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<Reservation> call, Throwable t) {
//                            Log.e("Error:", t.getMessage());
//                        }
//                    });
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
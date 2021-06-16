package com.example.buffetrestaurent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

public class AddReservation extends AppCompatActivity {
    int numsOftickets;
    Button plus;
    Button minus;
    TextView timepick;
    TextView tickets;
    TextView price;
    EditText name;
    EditText phone;
    DecimalFormat vnd = new DecimalFormat("###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        getSupportActionBar().hide();
        timepick = findViewById(R.id.AddReservation_txtTimePick);
        plus = findViewById(R.id.AddReservation_btnIncrease);
        minus = findViewById(R.id.AddReservation_btnDecrease);
        tickets = findViewById(R.id.AddReservation_txtNumTickets);
        price = findViewById(R.id.AddReservation_txtPrice);
        numsOftickets = Integer.parseInt(tickets.getText().toString());
        name = findViewById(R.id.AddReservation_inputName);
        phone = findViewById(R.id.AddReservation_inputPhone);
        name.setHint("Enter your name");
        phone.setHint("Enter your phone number");
        price.setText(vnd.format(numsOftickets * 200000) + " VND");
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
                        timepick.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
                timepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timepicker.setTitle("Pick Time");
                timepicker.show();
            }
        });
    }
}
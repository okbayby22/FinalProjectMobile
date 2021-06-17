package com.example.buffetrestaurent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.ReservationService;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

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
    String date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        getSupportActionBar().hide();
        timepick = findViewById(R.id.AddReservation_txtTimePick);
        plus = findViewById(R.id.AddReservation_btnIncrease);
        minus = findViewById(R.id.AddReservation_btnDecrease);
        add = findViewById(R.id.AddReservation_btnAdd);
        tickets = findViewById(R.id.AddReservation_txtNumTickets);
        price = findViewById(R.id.AddReservation_txtPrice);
        numsOftickets = Integer.parseInt(tickets.getText().toString());
        name = findViewById(R.id.AddReservation_inputName);
        phone = findViewById(R.id.AddReservation_inputPhone);
        calendarView = findViewById(R.id.AddReservation_CalendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = year+"-"+month+"-"+dayOfMonth;
                System.out.println("===========================>"+date);
            }
        });
        name.setHint("Enter your name");
        phone.setHint("Enter your phone number");
        price.setText(vnd.format(numsOftickets * 200000) + " VND");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deskid=0;
                int numogticket = Integer.parseInt(tickets.getText().toString());
                Double amount  = new Double(numsOftickets * 200000);
                System.out.println("....................."+date);
                int  status = 0;
                String time = timepick.getText().toString();
                int cusid = 1;
                int discountid = 1;
                int staffid = 0;
                Reservation reservation =  new Reservation(null,time,status,numogticket,amount,deskid,cusid,discountid,staffid);
                service = Apis.getReservationService();
                Call<Reservation> call = service.addReservation(reservation,date);
                System.out.println("..............."+reservation.getReservationDate());
                call.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(v.getContext(),"Add successful !",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                        Log.e("Error:",t.getMessage());
                    }
                });
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
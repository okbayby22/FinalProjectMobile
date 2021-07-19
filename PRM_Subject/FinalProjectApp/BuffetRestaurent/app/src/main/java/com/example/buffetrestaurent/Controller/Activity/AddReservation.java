package com.example.buffetrestaurent.Controller.Activity;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.CustomerService;
import com.example.buffetrestaurent.Utils.ReservationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReservation extends AppCompatActivity {
    int numsOftickets; //number of tickets
    Button plus; //plus button
    Button minus; // minus button
    Button add; // add button
    TextView timepick; //time picker to pick time of reservation
    TextView tickets; //show current tickets of reservation
    TextView price; //current price of reservation
    EditText name; //show name of customer
    EditText phone; //show phone of customer
    CalendarView calendarView;
    DecimalFormat vnd = new DecimalFormat("###,###"); //Format of balance
    String date; //date of reservation
    String email; //email of customer
    Customer customerInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strAddRes);
        timepick = findViewById(R.id.AddReservation_txtTimePick); //Text to show time
        plus = findViewById(R.id.AddReservation_btnIncrease); //Plus button to increase number of tickets
        minus = findViewById(R.id.AddReservation_btnDecrease); //Minus button to increase number of tickets
        add = findViewById(R.id.AddReservation_btnAdd); //Add button to add new reservation
        tickets = findViewById(R.id.AddReservation_txtNumTickets); //Tickets textview to show number of tickets
        price = findViewById(R.id.AddReservation_txtPrice); //Price textview to show total balance that user must pay
        numsOftickets = Integer.parseInt(tickets.getText().toString()); //Variable to save data of number of tickets
        price.setText(vnd.format(numsOftickets*200000)+" VND"); //Set value for textview
        name = findViewById(R.id.AddReservation_inputName); //Input text field phone
        phone = findViewById(R.id.AddReservation_inputPhone); //Input text field name
        calendarView = findViewById(R.id.AddReservation_CalendarView); //Calendar view
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date curdate = Calendar.getInstance().getTime(); //Get current day
        /*
        Set Name Input is uneditable
         */
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        name.setClickable(false);
        /*
        Set Phone Input is uneditable
         */
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        phone.setClickable(false);
        date = sdf.format(curdate);
        email = getIntent().getStringExtra("USER_EMAIL");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get data and binding to edit text
         */
        db.collection("customers")
                .whereEqualTo("customerEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                               if(task.isSuccessful() && !task.getResult().isEmpty()){
                                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                                    customerInfor = doc.toObject(Customer.class);
                                                    name.setText(customerInfor.getCustomerName());
                                                    phone.setText(customerInfor.getCustomerPhone());
                                               }
                                           }
                                       });
        /*
        Set Event on Date Change on Calendar View
         */
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                System.out.println(dayOfMonth);
                                date = (month + 1) + "/" + dayOfMonth + "/" + year;
                                System.out.println(date);
                            }
                        });
        /*
        Set Event on Click on Button Add
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numogticket = Integer.parseInt(tickets.getText().toString());
                /*
                If Customer does not pick time
                 */
                if (timepick.getText().toString().equals("Touch Here To Pick Time")) {
                    new AlertDialog.Builder(AddReservation.this).setTitle("Pick Time")
                            .setMessage("Please pick a time")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    /*
                    Check balance of customer
                     */
                    db.collection("customers")
                            .whereEqualTo("customerEmail", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                        String docID = doc.getId();
                                        Customer cus = doc.toObject(Customer.class);
                                        if(cus.getCustomerBalance() < numogticket*200000){
                                            new AlertDialog.Builder(AddReservation.this).setTitle("Add Reservation Notice")
                                                    .setMessage("You don't have enought balance")
                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    }).show();
                                        }else{
                                            /*
                                            Intent data to payment checkout
                                             */
                                            Intent intent = new Intent(v.getContext(), Payment.class);
                                            intent.putExtra("USER_EMAIL", email);
                                            intent.putExtra("PRICE", numogticket*200000);
                                            intent.putExtra("TICKET", numogticket);
                                            intent.putExtra("DATE", date);
                                            intent.putExtra("TIME", timepick.getText().toString());
                                            intent.putExtra("CUSTOMER", docID);
                                            intent.putExtra("Payment_Intent","From_Add_Reservation");
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
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
                        timepick.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false);
                timepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timepicker.setTitle("Pick Time");
                timepicker.show();
            }
        });
    }

    /**
     * Method of button OnClick event
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePage.class );
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }
}
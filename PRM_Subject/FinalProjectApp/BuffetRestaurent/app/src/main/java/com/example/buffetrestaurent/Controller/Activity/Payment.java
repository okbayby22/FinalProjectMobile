package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {


    DecimalFormat vnd = new DecimalFormat("###,###");
    TextView price;
    TextView finalprice;
    TextView discount;
    int payprice;
    int intentprice;
    int discountprice;
    String email;
    String date;
    String time;
    int ticket;
    String cusID;
    Button Checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strCheckout);
        intentprice = getIntent().getIntExtra("PRICE",0);
        discountprice = 0;
        email = getIntent().getStringExtra("USER_EMAIL");
        price = findViewById(R.id.Payment_txtTotal);
        finalprice = findViewById(R.id.Payment_txtFinalPrice);
        discount = findViewById(R.id.Payment_txtDiscount);
        Checkout = findViewById(R.id.Payment_btnCheckout);
        discount.setText(0+" VND");
        price.setText(vnd.format(intentprice)+" VND");
        payprice = intentprice - discountprice;
        finalprice.setText(vnd.format(payprice)+ " VND");
        ticket = getIntent().getIntExtra("TICKET",0);
        date = getIntent().getStringExtra("DATE");
        time = getIntent().getStringExtra("TIME");
        cusID= getIntent().getStringExtra("CUSTOMER");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("reservationId", "");
                user.put("reservationDate", date);
                user.put("reservationTime", time);
                user.put("reservationStatus", 0);
                user.put("numberTickets", ticket);
                user.put("reservationAmount", payprice);
                user.put("deskId","");
                user.put("customerId", cusID);
                user.put("discountId", "");
                user.put("staffId", "");
                db.collection("reservations")
                        .add(user)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                Toast.makeText(Payment.this, "Checkout Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(v.getContext(), HomePage.class);
                                intent.putExtra("USER_EMAIL", email);
                                startActivity(intent);
                            }
                        });
            }
        });

    }
}
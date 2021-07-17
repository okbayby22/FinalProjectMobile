package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.StaffManageAdapter;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.Model.DiscountInventory;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {


    DecimalFormat vnd = new DecimalFormat("###,###");
    TextView price;
    TextView finalprice;
    TextView discount;
    double payprice;
    int intentprice;
    int discountprice;
    String email;
    String date;
    String time;
    int ticket;
    TextView discountCode;
    EditText code;
    String cusID;
    Button Checkout;
    ArrayList<DiscountInventory> listInventory;
    ArrayList<Discount> listDiscount;
    String intentID;
    String intentDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strCheckout);
        intentprice = getIntent().getIntExtra("PRICE", 0);
        discountprice = 0;
        email = getIntent().getStringExtra("USER_EMAIL");
        price = findViewById(R.id.Payment_txtTotal);
        finalprice = findViewById(R.id.Payment_txtFinalPrice);
        discount = findViewById(R.id.Payment_txtDiscount);
        Checkout = findViewById(R.id.Payment_btnCheckout);
        discount.setText(0 + " VND");
        price.setText(vnd.format(intentprice) + " VND");
        payprice = intentprice - discountprice;
        finalprice.setText(vnd.format(payprice) + " VND");
        ticket = getIntent().getIntExtra("TICKET", 0);
        date = getIntent().getStringExtra("DATE");
        time = getIntent().getStringExtra("TIME");
        cusID = getIntent().getStringExtra("CUSTOMER");
        discountCode = findViewById(R.id.Payment_txtSeeDiscount);
        code = findViewById(R.id.Payment_txtCode);
        intentID = getIntent().getStringExtra("Payment_Intent");
        if(intentID.equals("From_Add_Reservation")){
            discount.setText("");
        }else{
            intentDiscount = getIntent().getStringExtra("Discount_Code");
            if(!intentDiscount.equals("")){
                code.setText(intentDiscount);
            }else{
                code.setText("");
            }

        }
        discountCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerDiscountHistoryActivity.class);
                intent.putExtra("IntentID","From_Payment");
                intent.putExtra("Date",date);
                intent.putExtra("Time",time);
                intent.putExtra("Tickets",ticket);
                intent.putExtra("PRICE",intentprice);
                intent.putExtra("CustomerID",cusID);
                intent.putExtra("USER_EMAIL",email);
                startActivity(intent);
            }
        });
//        loadDiscount();
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
                user.put("deskId", "");
                user.put("customerId", cusID);
                user.put("discountId", "");
                user.put("staffId", "");

                db.collection("customers")
                        .whereEqualTo("customerEmail", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    Customer cus = doc.toObject(Customer.class);
                                    double balance = cus.getCustomerBalance() - payprice;
                                    int point = cus.getCustomerPoint() + (ticket*20);
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("customerBalance", balance);
                                    data.put("customerPoint",point);
                                    db.collection("customers")
                                            .document(cusID)
                                            .update(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
//                                                    db.collection("reservations")
//                                                            .add(user)
//                                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                                                @Override
//                                                                public void onComplete(DocumentReference documentReference) {
//                                                                    if(task.isSuccessful()){
//                                                                        Map<String ,Object> data =  new HashMap<>();
//                                                                        data.put("customerId",documentReference.getId());
//                                                                    }
//                                                                    Toast.makeText(Payment.this, "Checkout Successfully", Toast.LENGTH_SHORT).show();
//                                                                    Intent intent = new Intent(v.getContext(), HomePage.class);
//                                                                    intent.putExtra("USER_EMAIL", email);
//                                                                    startActivity(intent);
//                                                                }
//                                                            });
                                                    db.collection("reservations")
                                                            .add(user)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Map<String ,Object> data =  new HashMap<>();
                                                                    data.put("reservationId",documentReference.getId());
                                                                    db.collection("reservations").document(documentReference.getId())
                                                                            .update(data);
                                                                    Toast.makeText(Payment.this, "Checkout Successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(v.getContext(), HomePage.class);
                                                                    intent.putExtra("USER_EMAIL", email);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }
                        });
            }
        });

    }
//    public void loadDiscountInvetory(){
//        listDiscount= new ArrayList<>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("customers")
//                .whereEqualTo("customerEmail", email)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
//                            Customer cus = doc.toObject(Customer.class);
//                            db.collection("discount_inventory")
//                                    .whereEqualTo("customerId", cus.getCustomerId())
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                                                for (QueryDocumentSnapshot doc : task.getResult()) {
//                                                    DiscountInventory dis = doc.toObject(DiscountInventory.class);
//                                                    listInventory.add(dis);
//                                                }
//                                            }
//                                        }
//                                    });
//                        }
//
//                    }
//                });
//    }
//    public void loadDiscount(){
//        loadDiscountInvetory();
//        for (int i=0;i<listInventory.size();i++){
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.collection("discount")
//                    .whereEqualTo("discountId", listInventory.get(i).getDiscountId())
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
//                                listDiscount.add(doc.toObject(Discount.class));
//                            }
//
//                        }
//                    });
//        }
//    }
}
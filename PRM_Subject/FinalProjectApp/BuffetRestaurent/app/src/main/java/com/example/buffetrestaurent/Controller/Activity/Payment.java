package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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
    TextView price; //Price of reservation
    TextView finalprice; //Price that customer pay after checkout
    TextView discount,displayDiscount,wrongCode;
    double payprice; //Variable to store balance customer must pay after checkout
    double intentprice; //Variable to store price that get from another activity
    double discountprice; //Variable to store discount price of reservation
    String email; //Email of user
    String date; // Date of reservation
    String time; //Time of reservation
    String discoutString; //Discount code
    int ticket; // Number of tickets of reservation
    TextView chooseDiscount; //View that press to choose discount
    EditText txtcode; //Discount code input
    String cusID; //Customer ID
    Button Checkout,discountSubmit; //Checkout: button to checkout, discountSubmit: button to apply discount
    ArrayList<DiscountInventory> listInventory;
    ArrayList<Discount> listDiscount;
    String intentID; //Throw intentID to another activity to check
    String intentDiscount; //Discount code get from another activity
    boolean checkDiscount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strCheckout); //Set title for supported bar

        displayDiscount = findViewById(R.id.Payment_txtDiscount);
        intentprice = Double.valueOf(getIntent().getIntExtra("PRICE", 0)); //get price from another activity
        discountprice = 0; //Initialize value for discount price
        email = getIntent().getStringExtra("USER_EMAIL"); //get email of user
        /*
        Mapping view with layout
         */
        price = findViewById(R.id.Payment_txtTotal); //Show price of reservation
        finalprice = findViewById(R.id.Payment_txtFinalPrice); //Show final price that customer must pay after checkout
        discount = findViewById(R.id.Payment_txtDiscount); //Show discount price
        Checkout = findViewById(R.id.Payment_btnCheckout); //Checkout button
        discountSubmit = findViewById(R.id.Payment_btnSubmit); //Apply discount button
        wrongCode = findViewById(R.id.Payment_txtWrongCode); //Notice of wrong discount code
        discount.setText(0 + " VND"); //Set default text for discount price
        price.setText(vnd.format(intentprice) + " VND"); //Set text for price of reservation
        payprice = intentprice - discountprice; //Calculate balance that customer must pay after checkout
        finalprice.setText(vnd.format(payprice) + " VND"); //Set final price to show to customer
        /*
        Get value from another activity
         */
        ticket = getIntent().getIntExtra("TICKET", 0); //Number of tickets of reservation
        date = getIntent().getStringExtra("DATE"); //Date of reservation
        time = getIntent().getStringExtra("TIME"); //Time of reservation
        cusID = getIntent().getStringExtra("CUSTOMER"); //Customer ID
        chooseDiscount = findViewById(R.id.Payment_txtSeeDiscount); //Click to choose discount from discount inventory
        txtcode = findViewById(R.id.Payment_txtCode); //Discount code input
        intentID = getIntent().getStringExtra("Payment_Intent"); //Get intent from another activity to check
        if(intentID.equals("From_Add_Reservation")){ //If payment was intented from Add reservation
            txtcode.setText("");
        }else{
            intentDiscount = getIntent().getStringExtra("Discount_Code"); //Get discount code from discount inventory (copy to clipboard)
            if(!intentDiscount.equals("")){
                txtcode.setText(intentDiscount);
            }else{
                txtcode.setText("");
            }
        }

        discoutString = txtcode.getText().toString();

        //Press Your discount to discount inventory to choose discount
        chooseDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerDiscountHistoryActivity.class);
                intent.putExtra("IntentID","From_Payment");
                intent.putExtra("DATE",date);
                intent.putExtra("TIME",time);
                intent.putExtra("TICKET",ticket);
                intent.putExtra("PRICE",intentprice);
                intent.putExtra("CustomerID",cusID);
                intent.putExtra("USER_EMAIL",email);
                startActivity(intent);
                finish();
            }
        });


        loadDiscountInvetory();//Load list of discount that customer own
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Press checkout button
         */
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
                user.put("discountId", discoutString);
                user.put("staffId", "");
                db.collection("customers")
                        .whereEqualTo("customerEmail", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            //Get customer success
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    Customer cus = doc.toObject(Customer.class);
                                    double balance = cus.getCustomerBalance() - payprice;
                                    /*
                                    Change balance of customer
                                     */
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("customerBalance", balance);
                                    db.collection("customers")
                                            .document(cusID)
                                            .update(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
//
                                                    db.collection("reservations")
                                                            .add(user)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Map<String ,Object> data =  new HashMap<>();
                                                                    data.put("reservationId",documentReference.getId());
                                                                    db.collection("reservations").document(documentReference.getId())
                                                                            .update(data);
                                                                    if(!txtcode.getText().toString().equals("") && checkDiscount == true){
                                                                        db.collection("discount_inventory")
                                                                                .whereEqualTo("discountId",txtcode.getText().toString())
                                                                                .whereEqualTo("customerId",cusID)
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                                                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                                                                                            /*
                                                                                            Delete discount when apply success
                                                                                             */
                                                                                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                                                                            String discountIventID = doc.getId();
                                                                                            db.collection("discount_inventory")
                                                                                                    .document(discountIventID)
                                                                                                    .delete();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
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


        /*
        Apply discount button
         */
        discountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCode = txtcode.getText().toString();
                checkDiscount = false;
                for(int i =0;i<listDiscount.size();i++){
                    if(listDiscount.get(i).getDiscountId().equals(getCode)){//if code that user input match with code of discount
                        discoutString=getCode;
                        discountprice = Double.valueOf(intentprice)*(Double.valueOf(listDiscount.get(i).getDiscountPercent())/100);// update discount price
                        displayDiscount.setText(vnd.format(discountprice) + " VND");//display discount price to screen
                        payprice = intentprice - discountprice;//minus total price and discount price if has
                        finalprice.setText(vnd.format(payprice) + " VND");//display total price after minus with discount if has
                        checkDiscount=true;
                        break;
                    }else{
                        checkDiscount=false;
                    }
                }
                if(checkDiscount==false){
                    discoutString="";
                    discountprice=0;
                    displayDiscount.setText(vnd.format(discountprice) + " VND");
                    wrongCode.setText("Code is not valid");
                    Checkout.setClickable(false);
                    Checkout.setAlpha((float) 0.3);
                    payprice = intentprice - discountprice;
                    finalprice.setText(vnd.format(payprice) + " VND");
                }else{
                    wrongCode.setText("");
                    txtcode.setFocusable(false);
                    txtcode.setFocusableInTouchMode(false);
                    txtcode.setClickable(false);
                    txtcode.setAlpha((float) 0.3);
                    discountSubmit.setClickable(false);
                    discountSubmit.setAlpha((float) 0.3);
                }
            }
        });
    }




    public void loadDiscountInvetory(){
        listDiscount= new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .whereEqualTo("customerEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            Customer cus = doc.toObject(Customer.class);
                            db.collection("discount_inventory")
                                    .whereEqualTo("customerId", cus.getCustomerId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                    DiscountInventory dis = doc.toObject(DiscountInventory.class);
                                                    loadDiscount(dis.getDiscountId());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    public void loadDiscount(String id){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("discount")
                    .whereEqualTo("discountId", id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                listDiscount.add(doc.toObject(Discount.class));
                            }

                        }
                    });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , AddReservation.class );
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }
    /**
     * Event of back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , AddReservation.class );
        intent.putExtra("USER_EMAIL", email);
        startActivity(intent);
        this.finish();
    }
}
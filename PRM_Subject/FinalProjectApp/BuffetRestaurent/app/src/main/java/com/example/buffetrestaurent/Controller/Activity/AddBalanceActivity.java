package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddBalanceActivity extends AppCompatActivity {


    EditText email, balance;
    Button submit, add;
    TextView emailError, balanceError;
    String userEmail;
    double staffRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strAddBalance);

        /*
        Mapping view with layout
         */
        email = findViewById(R.id.AddBalance_txtEmail); //Input customer email
        email.setHint("Email");

        userEmail= getIntent().getStringExtra("USER_EMAIL"); //Get staff email
        staffRole = getIntent().getDoubleExtra("ROLE",0);

        emailError = findViewById(R.id.AddBalance_txtEmail_Error); //Show email error or customer's balance
        balanceError = findViewById(R.id.AddBalance_txtAddBalance_Error); //Show balance Error or after add balance
        balance = findViewById(R.id.AddBalance_txtBalance); //Input balance
        /*
        Set balance input disable when user doesn't input email or wrong email
         */
        balance.setFocusable(false);
        balance.setFocusableInTouchMode(false);
        balance.setClickable(false);
        balance.setAlpha((float) 0.3);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submit = findViewById(R.id.AddBalance_btnAdd); //Button press to check email
        /*
        Disable button add balance before user input balance wants to add
         */
        add = findViewById(R.id.AddBalance_btnAddBalance);
        add.setClickable(false);
        add.setAlpha((float) 0.3);
        /*
        Press button submit to check email
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("customers")
                        .whereEqualTo("customerEmail", email.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    /*
                                    Enable balance input
                                     */
                                    balance.setTextColor(Color.BLACK);
                                    balance.setFocusable(true);
                                    balance.setFocusableInTouchMode(true);
                                    balance.setClickable(true);
                                    balance.setAlpha((float) 1);
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    Customer cus = doc.toObject(Customer.class);
                                    emailError.setText("Your balance: " + cus.getCustomerBalance()); //Print customer's current balance
                                    balance.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        /*
                                        Show balance of customer after add when text change
                                         */
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            if (!balance.getText().toString().equals("")) {
                                                add.setClickable(true);
                                                add.setAlpha(1);
                                            }
                                            if (balance.getText().toString().equals("")) {
                                                balanceError.setText("");
                                            } else if (Integer.parseInt(balance.getText().toString()) < 1000) {
                                                add.setClickable(false);
                                                add.setAlpha((float) 0.3);
                                            } else {
                                                balanceError.setTextSize(15);
                                                balanceError.setText("Your balance after add " + (cus.getCustomerBalance() + Double.parseDouble(balance.getText().toString())));
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    /*
                                    Press add button to add balance for customer
                                     */
                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            double newBalance = cus.getCustomerBalance() + Double.parseDouble(balance.getText().toString());
                                            /*
                                            Update new balance for customer
                                             */
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("customerBalance", newBalance);
                                            db.collection("customers")
                                                    .document(cus.getCustomerId())
                                                    .update(data)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                new AlertDialog.Builder(AddBalanceActivity.this).setTitle("Add Balance Notice").setMessage("Add Balance")
                                                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            /*
                                                                            If press OK, set form to default
                                                                             */
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                emailError.setTextColor(Color.BLACK);
                                                                                email.setText("");
                                                                                email.setAlpha(1);
                                                                                emailError.setText("");
                                                                                balance.setText("");
                                                                                balance.setFocusable(false);
                                                                                balance.setFocusableInTouchMode(false);
                                                                                balance.setClickable(false);
                                                                                add.setClickable(false);
                                                                                add.setAlpha((float) 0.3);
                                                                            }
                                                                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                                    @Override
                                                                    /*
                                                                    Click out side to close dialog
                                                                     */
                                                                    public void onCancel(DialogInterface dialog) {
                                                                        emailError.setTextColor(Color.BLACK);
                                                                        email.setText("");
                                                                        email.setAlpha(1);
                                                                        emailError.setText("");
                                                                        balance.setText("");
                                                                        balance.setFocusable(false);
                                                                        balance.setFocusableInTouchMode(false);
                                                                        balance.setClickable(false);
                                                                        add.setClickable(false);
                                                                        add.setAlpha((float) 0.3);
                                                                    }
                                                                }).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                } else {
                                    /*
                                    Email invalid
                                     */
                                    emailError.setTextColor(Color.RED);
                                    emailError.setText("Email is invalid");
                                    balance.setFocusable(false);
                                    balance.setFocusableInTouchMode(false);
                                    balance.setClickable(false);
                                    balance.setAlpha((float) 0.3);
                                    add.setClickable(false);
                                    add.setFocusableInTouchMode(false);
                                    add.setAlpha((float) 0.3);
                                    balance.setText("");
                                    balanceError.setText("");
                                }
                            }
                        });
            }
        });
    }
    @Override
    /*
    Back button on supported bar
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", staffRole);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", staffRole);
        startActivity(intent);
        this.finish();
    }
}
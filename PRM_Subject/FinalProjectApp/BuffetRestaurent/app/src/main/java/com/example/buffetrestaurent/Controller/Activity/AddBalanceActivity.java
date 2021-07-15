package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        email = findViewById(R.id.AddBalance_txtEmail);
        email.setHint("Email");
        emailError = findViewById(R.id.AddBalance_txtEmail_Error);
        balanceError = findViewById(R.id.AddBalance_txtAddBalance_Error);
        balance = findViewById(R.id.AddBalance_txtBalance);
        balance.setFocusable(false);
        balance.setFocusableInTouchMode(false);
        balance.setClickable(false);
        balance.setAlpha((float) 0.3);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submit = findViewById(R.id.AddBalance_btnAdd);
        add = findViewById(R.id.AddBalance_btnAddBalance);
        add.setClickable(false);
        add.setAlpha((float) 0.3);
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
                                    balance.setTextColor(Color.BLACK);
                                    balance.setFocusable(true);
                                    balance.setFocusableInTouchMode(true);
                                    balance.setClickable(true);
                                    balance.setAlpha((float) 1);
                                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                                    Customer cus = doc.toObject(Customer.class);
                                    emailError.setText("Your balance: " + cus.getCustomerBalance());
                                    balance.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

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

                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            System.out.println(">>>>>>>>>>>>>>> Toi day");
                                            double newBalance = cus.getCustomerBalance() + Double.parseDouble(balance.getText().toString());
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("customerBalance", newBalance);
                                            db.collection("customers")
                                                    .document(cus.getCustomerId())
                                                    .update(data)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            System.out.println(">>>>>>>>>>>> Vao on complete");
                                                            if (task.isSuccessful()) {
                                                                System.out.println(">>>>>>>>>> Vo toi vong if trong Complete");
                                                                new AlertDialog.Builder(AddBalanceActivity.this).setTitle("Add Balance Notice").setMessage("Add Balance")
                                                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
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
                                                                    public void onCancel(DialogInterface dialog) {
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
}
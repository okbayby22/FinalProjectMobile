package com.example.buffetrestaurent.Controller.Activity;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.CustomerService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfile extends AppCompatActivity {

    String userEmail,userID;
    Customer customerInfor;
    TextView txtName,txtEmail,txtPhone,txtAddress;
    TextView txtNameError,txtPhoneError;
    //Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);

        /*
        Mapping view to layout
         */
        txtName=findViewById(R.id.userInfor_txtName);
        txtEmail=findViewById(R.id.userInfor_txtEmail);
        txtPhone=findViewById(R.id.userInfor_txtPhone);
        txtAddress=findViewById(R.id.userInfor_txtAddress);
        txtNameError=findViewById(R.id.userInfor_txtName_error);
        txtPhoneError=findViewById(R.id.userInfor_txtPhone_error);

        /*
        User can not edit email
         */
        txtEmail.setEnabled(false);
        txtEmail.setClickable(false);
        txtEmail.setAlpha((float) 0.3);
        customerInfor =new Customer();
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        loadData();
    }

    /**
     * Event of button on supported bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePage.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    /**
     * Load data of current user
     */
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Load data of current user by email
         */
        db.collection("customers")
                .whereEqualTo("customerEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                customerInfor = document.toObject(Customer.class);
                                /*
                                Binding to view
                                 */
                                txtName.setText(customerInfor.getCustomerName());
                                txtAddress.setText(customerInfor.getCustomerAddress());
                                txtPhone.setText(customerInfor.getCustomerPhone());
                                txtEmail.setText(customerInfor.getCustomerEmail());
                                userID = document.getId();
                            }
                        } else {

                        }
                    }
                });
    }

    /**
     * Method of button OnClick event
     * @param view
     */
    public void update_Click(View view) {
        boolean nameStatus=true;
        boolean phoneStatus=true;
        Pattern pattern = Pattern.compile("(0[1-9])+([0-9]{8})");
        Matcher matcher = pattern.matcher(txtPhone.getText().toString());
        if(txtName.getText().toString().isEmpty()){
            txtNameError.setText("Name can not empty !!!");
            nameStatus=false;
        }
        else{
            nameStatus=true;
            txtNameError.setText("");
        }
        if(txtPhone.getText().toString().isEmpty()){
            txtPhoneError.setText("Phone number can not empty !!!");
            phoneStatus=false;
        }
        else if(!matcher.matches()){
            txtPhoneError.setText("Wrong phone format !!! Ex: 0123456789");
            phoneStatus=false;
        }
        else{
            txtPhoneError.setText("");
            phoneStatus=true;
        }

        if(nameStatus&&phoneStatus){
            customerInfor.setCustomerName(txtName.getText().toString());
            customerInfor.setCustomerPhone(txtPhone.getText().toString());
            customerInfor.setCustomerAddress(txtAddress.getText().toString());
            updateToDB();
        }
    }

    /**
     * Update data to database
     */
    public void updateToDB(){
        /*
        Set update data to a hashmap
         */
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", txtName.getText().toString());
        data.put("customerAddress", txtAddress.getText().toString());
        data.put("customerEmail", txtEmail.getText().toString());
        data.put("customerPhone", txtPhone.getText().toString());

        List<String> fields= new ArrayList<String>();
        fields.add("customerName");
        fields.add("customerAddress");
        fields.add("customerEmail");
        fields.add("customerPhone");

        /*
        Update to database
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers").document(userID)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserProfile.this,"Update successful !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(UserProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
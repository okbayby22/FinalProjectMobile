package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.buffetrestaurent.Adapter.DiscountAdapter;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditDiscountStaffManage extends AppCompatActivity {
    //Contain ID of discount
    String discountID;
    //Edit Text component
    EditText edtDiscountName,edtDiscountPoint,edtDiscountPercent;
    //Text view Component
    TextView txtErrorName,txtErrorPoint,txtErrorPercent;
    //Button component
    Button btnSubmit,btnCancel;
    //Switch component
    Switch aSwitch;
    //contain list name of discount
    ArrayList<String> discountName;
    //Contain user email of using user
    String userEmail;
    //Contain staff ID has been achieved from user email
    String staffID;

    double role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount_staff_manage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strUpdateDiscount);
        //Find ID for component
        edtDiscountName = findViewById(R.id.editdiscount_edtName);
        edtDiscountPoint = findViewById(R.id.editdiscount_edtPoint);
        edtDiscountPercent = findViewById(R.id.editdiscount_edtPercent);
        btnSubmit = findViewById(R.id.editdiscount_btnSubmit);
        btnCancel = findViewById(R.id.editdiscount_btnCancel);
        txtErrorName = findViewById(R.id.editdiscount_txtErrorName);
        txtErrorPoint = findViewById(R.id.editdiscount_txtErrorPoint);
        txtErrorPercent = findViewById(R.id.editdiscount_txtErrorPercent);
        aSwitch = findViewById(R.id.editdiscount_switch);
        //Get intent from sender
        discountID = getIntent().getStringExtra("Discount_ID");
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        role = getIntent().getDoubleExtra("ROLE",0);
        //Create object for discountName arraylist
        discountName = new ArrayList<>();
        //Load all discount and set value for component
        loadDiscount(this);
        //load all discount name and store it in discountName arrayList
        loadDiscountName();
        //Load staff ID
        loadStaffId();
        //Create click envent for Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Intent to StaffDiscount managemnet
                Intent intent=new Intent(v.getContext(), StaffDiscountManagement.class);
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                finish();
            }
        });
        //Create event for submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check condition
                boolean checkName = false,checkPoint = false,checkPercent =false;
                //Check Name
                if(edtDiscountName.getText().toString().equals("")){
                    txtErrorName.setText("Name of discount cannot be empty!!");
                    txtErrorName.setTextColor(Color.RED);
                }else if(discountName.contains(edtDiscountName.getText().toString().toUpperCase())){
                    txtErrorName.setText("Name of discount has been existed!!");
                    txtErrorName.setTextColor(Color.RED);
                }else{
                    txtErrorName.setText("");
                    checkName = true;
                }
                //Check Points
                if(edtDiscountPoint.getText().toString().equals("")){
                    txtErrorPoint.setText("Points cannot be empty!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtDiscountPoint.getText().toString()) <=0 ){
                    txtErrorPoint.setText("Points cannot be less than 0!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtDiscountPoint.getText().toString()) >=1000 ){
                    txtErrorPoint.setText("Points cannot be more than 1000!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }
                else{
                    txtErrorPoint.setText("");
                    checkPoint= true;
                }
                //Check Percents
                if(edtDiscountPercent.getText().toString().equals("")){
                    txtErrorPercent.setText("Percent cannot be empty!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }
                else if(Integer.parseInt(edtDiscountPercent.getText().toString()) <=0 ){
                    txtErrorPercent.setText("Percent cannot be less than 0!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtDiscountPercent.getText().toString()) >100 ){
                    txtErrorPercent.setText("Percent cannot be more than 100!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }else{
                    txtErrorPercent.setText("");
                    checkPercent = true;
                }
                //Add Discount
                if(checkName && checkPoint && checkPercent){
                    UpdateDiscount();
                }
            }
        });
    }

    /**
     * load discount name
     */
    public void loadDiscountName(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){

                        }else{
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Discount discount = new Discount();
                                discount= document.toObject(Discount.class);
                                String dName = discount.getDiscountName();
                                if(!dName.equalsIgnoreCase(edtDiscountName.getText().toString())){
                                    discountName.add(dName.toUpperCase());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * load discount data that has discountID
     * @param context
     */
    public void loadDiscount(Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .whereEqualTo("discountId",discountID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){
                        }else{
                            Discount discount = new Discount();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                discount = document.toObject(Discount.class);
                            }
                            edtDiscountName.setText(discount.getDiscountName());
                            edtDiscountPoint.setText(String.valueOf(discount.getDiscountPoint()));
                            edtDiscountPercent.setText(String.valueOf(discount.getDiscountPercent()));
                            if(discount.getDiscountStatus()==1){
                               aSwitch.setChecked(true);
                            }else{
                                aSwitch.setChecked(false);
                            }
                        }

                    }
                });
    }

    /**
     * Update discount that has been sent
     */
    public void UpdateDiscount(){
        Map<String, Object> discountObject = new HashMap<>();
        discountObject.put("discountName",edtDiscountName.getText().toString());
        discountObject.put("staffId",staffID);
        discountObject.put("discountPercent",Integer.parseInt(edtDiscountPercent.getText().toString()));
        discountObject.put("discountPoint",Integer.parseInt(edtDiscountPoint.getText().toString()));
        if(aSwitch.isChecked()){
            discountObject.put("discountStatus",1);
        }else{
            discountObject.put("discountStatus",2);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .document(discountID)
                .update(discountObject);
        Intent intent=new Intent(this, StaffDiscountManagement.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
    }

    /**
     * Load staff id that has that user email
     */
    public void loadStaffId(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("staffEmail",userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){

                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                staffID = document.getId();
                            }
                        }
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
                Intent intent = new Intent(this , StaffDiscountManagement.class );
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , StaffDiscountManagement.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }
}
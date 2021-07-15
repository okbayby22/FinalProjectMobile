package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaffProfile extends AppCompatActivity {

    String email,ID;
    TextView txtName,txtEmail,txtPhone,txtAddress,txtGender;
    TextView txtNameError,txtPhoneError;
    Staff cus;
    ImageView avt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);
        txtName = findViewById(R.id.staffInfor_txtName);
        txtEmail = findViewById(R.id.staffInfor_txtEmail);
        txtPhone = findViewById(R.id.staffInfor_txtPhone);
        txtAddress = findViewById(R.id.staffInfor_txtAddress);
        txtGender = findViewById(R.id.staffInfor_txtGender);
        avt = findViewById(R.id.staffInfor_imgAvt);
        txtEmail.setFocusable(false);
        txtEmail.setFocusableInTouchMode(false);
        txtEmail.setClickable(false);
        txtGender.setFocusable(false);
        txtGender.setFocusableInTouchMode(false);
        txtGender.setClickable(false);
        txtNameError = findViewById(R.id.staffInfor_txtName_error);
        txtPhoneError = findViewById(R.id.staffInfor_txtPhone_error);
        ID = getIntent().getStringExtra("ID");
        email = getIntent().getStringExtra("EMAIL");
        loadData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , StaffManageActivity.class );
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("staffEmail",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            cus = doc.toObject(Staff.class);
                            txtName.setText(cus.getStaffName());
                            txtEmail.setText(email);
                            txtPhone.setText(cus.getStaffPhone());
                            txtAddress.setText(cus.getStaffAddress());
                            GradientDrawable imgshape = new GradientDrawable();
                            imgshape.setShape(GradientDrawable.OVAL);
                            imgshape.setStroke(3, Color.BLACK);
                            imgshape.setCornerRadius(100);
                            avt.setBackgroundDrawable(imgshape);
                            Picasso.get().load(cus.getStaffImage()).into(avt);
                            if(cus.getStaffGender() == 0){
                                txtGender.setText("Male");
                            }else if(cus.getStaffGender() == 1){
                                txtGender.setText("Female");
                            }
                            cus.setStaffId(ID);
                        }
                    }
                });
    }

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
            cus.setStaffName(txtName.getText().toString());
            cus.setStaffPhone(txtPhone.getText().toString());
            cus.setStaffAddress(txtAddress.getText().toString());
            updateToDB();
        }
    }

    private void updateToDB() {
        Map<String, Object> data = new HashMap<>();
        data.put("staffName", txtName.getText().toString());
        data.put("staffAddress", txtAddress.getText().toString());
        data.put("staffEmail", txtEmail.getText().toString());
        data.put("staffPhone", txtPhone.getText().toString());

        List<String> fields= new ArrayList<String>();
        fields.add("customerName");
        fields.add("customerAddress");
        fields.add("customerEmail");
        fields.add("customerPhone");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs").document(ID)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(StaffProfile.this,"Update staff successful !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(StaffProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    String discountID;
    EditText edtDiscountName,edtDiscountPoint,edtDiscountPercent;
    TextView txtErrorName,txtErrorPoint,txtErrorPercent;
    Button btnSubmit,btnCancel;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount_staff_manage);
        edtDiscountName = findViewById(R.id.editdiscount_edtName);
        edtDiscountPoint = findViewById(R.id.editdiscount_edtPoint);
        edtDiscountPercent = findViewById(R.id.editdiscount_edtPercent);
        btnSubmit = findViewById(R.id.editdiscount_btnSubmit);
        btnCancel = findViewById(R.id.editdiscount_btnCancel);
        txtErrorName = findViewById(R.id.editdiscount_txtErrorName);
        txtErrorPoint = findViewById(R.id.editdiscount_txtErrorPoint);
        txtErrorPercent = findViewById(R.id.editdiscount_txtErrorPercent);
        aSwitch = findViewById(R.id.editdiscount_switch);
        discountID = getIntent().getStringExtra("Discount_ID");
        loadDiscount(this);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), StaffDiscountManagement.class);
                startActivity(intent);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDiscountName.getText().toString().equals("")){
                    txtErrorName.setText("Name of discount cannot be empty!!");
                    txtErrorName.setTextColor(Color.RED);
                }else if(edtDiscountPoint.getText().toString().equals("")){
                    txtErrorName.setText("");
                    txtErrorPoint.setText("Points cannot be empty!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }else if(edtDiscountPercent.getText().toString().equals("")){
                    txtErrorPoint.setText("");
                    txtErrorPercent.setText("Percent cannot be empty!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }
                else{


                    txtErrorPercent.setText("");
                    UpdateDiscount();
                }
            }
        });
    }
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

    public void UpdateDiscount(){
        Map<String, Object> discountObject = new HashMap<>();
        discountObject.put("discountName",edtDiscountName.getText().toString());
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
        startActivity(intent);
    }
}
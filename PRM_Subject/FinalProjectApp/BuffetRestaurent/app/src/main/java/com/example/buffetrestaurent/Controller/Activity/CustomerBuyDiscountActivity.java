package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.BuyDiscountListAdapter;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerBuyDiscountActivity extends AppCompatActivity implements BuyDiscountListAdapter.OnBtnBuyClickListener{

    TextView txtCurrentPoint;
    RecyclerView discountRecyclerView;
    String userEmail;
    private List<Discount> discountList = new ArrayList<>();
    private BuyDiscountListAdapter dAdapter;
    double customerPoint;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_buy_discount);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        discountRecyclerView =findViewById(R.id.customer_buy_discount_DiscountListView);
        txtCurrentPoint = findViewById(R.id.customer_buy_discount_txtCurrentPoint);
        dAdapter = new BuyDiscountListAdapter(discountList,this,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        discountRecyclerView.setLayoutManager(mLayoutManager);
        discountRecyclerView.setItemAnimator(new DefaultItemAnimator());
        discountRecyclerView.setAdapter(dAdapter);
        loadData();
    }

    private void loadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .whereEqualTo("customerEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                customerPoint = document.getDouble("customerPoint");
                                userID = document.getId();
                                txtCurrentPoint.setText(txtCurrentPoint.getText()+" "+customerPoint);
                                loadDisocunt();
                            }
                        } else {

                        }
                    }
                });
    }

    private void loadDisocunt(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .whereEqualTo("discountStatus", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                discountList.add(document.toObject(Discount.class));
                            }
                            dAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onBtnBuyClick(int position) {
        checkDuplicateDiscount(position);
    }

    public void checkDuplicateDiscount(int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount_inventory")
                .whereEqualTo("customerId", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean check=true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(discountList.get(position).getDiscountId().equalsIgnoreCase(document.getString("discountId"))){
                                    check = false;
                                }
                            }
                            if(check){
                                buyDiscount(position);
                            }
                            else{
                                AlertDialog errorDialog=new AlertDialog.Builder(CustomerBuyDiscountActivity.this)
                                        .setTitle("Warning")
                                        .setMessage("You have already own this discount !!!")
                                        .setNegativeButton(R.string.strOK, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create();
                                errorDialog.show();
                            }
                            dAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void buyDiscount(int position){
        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Do you want to buy this discount ?")
                .setPositiveButton(R.string.strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(customerPoint < discountList.get(position).getDiscountPoint()){
                            AlertDialog errorDialog=new AlertDialog.Builder(CustomerBuyDiscountActivity.this)
                                    .setTitle("Warning")
                                    .setMessage("You don't have enough point !!!")
                                    .setNegativeButton(R.string.strOK, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create();
                            errorDialog.show();
                        }
                        else{
                            customerPoint = customerPoint - discountList.get(position).getDiscountPoint();
                            txtCurrentPoint.setText("Your Current Point : "+customerPoint);
                            Map<String ,Object> data =  new HashMap<>();
                            data.put("customerId", userID);
                            data.put("discountId", discountList.get(position).getDiscountId());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("discount_inventory")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Map<String ,Object> data =  new HashMap<>();
                                            data.put("customerPoint",customerPoint);
                                            db.collection("customers").document(userID)
                                                    .update(data);
                                            Toast.makeText(CustomerBuyDiscountActivity.this,"Buy successful !",Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton(R.string.strNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alertDialog.show();
    }
}
package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.BuyDiscountListAdapter;
import com.example.buffetrestaurent.Adapter.HistoryDiscountListAdapter;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerDiscountHistoryActivity extends AppCompatActivity implements HistoryDiscountListAdapter.OnBtnCopyClickListener {

    RecyclerView discountHistoryRecyclerView;
    String userEmail;
    private List<Discount> discountList = new ArrayList<>();
    private List<String> discountIdList = new ArrayList<>();
    private HistoryDiscountListAdapter dHisAdapter;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_discount_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strMenu);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        discountHistoryRecyclerView =findViewById(R.id.customer_history_discount_DiscountListView);

        dHisAdapter = new HistoryDiscountListAdapter(discountList,this,this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        discountHistoryRecyclerView.setLayoutManager(mLayoutManager);
        discountHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        discountHistoryRecyclerView.setAdapter(dHisAdapter);
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
                                userID = document.getId();
                                loadDisocuntId();
                            }
                        } else {

                        }
                    }
                });
    }

    public void loadDisocuntId(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount_inventory")
                .whereEqualTo("customerId", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //discountIdList.add(document.getString("discountId"));
                                loadDiscount(document.getString("discountId"));
                            }

                        } else {

                        }
                    }
                });
    }

    public void loadDiscount(String discountId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .whereEqualTo("discountId", discountId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                discountList.add(document.toObject(Discount.class));
                            }
                            dHisAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                });
    }

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

    @Override
    public void onBtnCopyClick(int position) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", discountList.get(position).getDiscountId());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Discount code copied", Toast.LENGTH_SHORT).show();
    }
}
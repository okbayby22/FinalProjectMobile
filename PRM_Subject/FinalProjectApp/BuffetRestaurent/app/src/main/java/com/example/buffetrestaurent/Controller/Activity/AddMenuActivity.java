package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.buffetrestaurent.Adapter.AddFoodToMenuAdapter;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    String userEmail;
    ArrayList<Food> listFood;
    AddFoodToMenuAdapter adapter;
    RecyclerView recyclerView;
    String date,menuID;
    Button addtoMenu;
    double role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strMenuStaffAddFood);
        recyclerView = findViewById(R.id.addmenu_listFood);
        addtoMenu =findViewById(R.id.addmenu_button);
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        date= getIntent().getStringExtra("SELECTED_DATE");
        role = getIntent().getDoubleExtra("ROLE",0);

        listFood=new ArrayList<>();
        loadData();
        loadMenuID();

    }
    public void loadMenuID(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu")
                .whereEqualTo("menuDate",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){
                            loadStaffId();
                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                menuID = document.getId();
                            }
                        }
                    }
                });
    }
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
                                addnewMenu(document.getId());
                            }
                        }
                    }
                });
    }
    public void addnewMenu(String Id){
        Map<String ,Object> data =  new HashMap<>();
        data.put("menuDate",date);
        data.put("menuId","");
        data.put("staffId",Id);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String ,Object> data =  new HashMap<>();
                        data.put("menuId",documentReference.getId());
                        db.collection("menu").document(documentReference.getId())
                                .update(data);
                        menuID = documentReference.getId();
                    }
                });
    }
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listFood.add(document.toObject(Food.class));
                            }
                            //adapter.notifyDataSetChanged();
                            //recyclerView.setLayoutManager(new LinearLayoutManager(AddMenuActivity.this));

                            adapter = new AddFoodToMenuAdapter(AddMenuActivity.this,listFood);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager mLayoutManager = new GridLayoutManager(AddMenuActivity.this,1,GridLayoutManager.VERTICAL,false);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setHasFixedSize(true);
                            recyclerView.addItemDecoration(new DividerItemDecoration(AddMenuActivity.this,DividerItemDecoration.VERTICAL));

                        }
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , MenuMangeForStaff.class );
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
        Intent intent = new Intent(this , MenuMangeForStaff.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }

    public void addtoMenu(View view){

        for(Food f: listFood){
            if(f.isSelected()){
                Map<String ,Object> data =  new HashMap<>();
                data.put("foodId",f.getFoodId());
                data.put("menuId",menuID);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("menu_detail")
                        .add(data);
            }
        }
        Intent intent=new Intent(this, MenuMangeForStaff.class);
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        finish();
    }
}
package com.example.buffetrestaurent.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Controller.Activity.CustomerProfile;
import com.example.buffetrestaurent.Controller.Activity.UpdateFoodActivity;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaffManageFoodAdapter extends RecyclerView.Adapter<StaffManageFoodAdapter.ViewHolder>{
    //store list of food from database
    private ArrayList<Food> list;
    //store context from activity
    private Context context;
    //store email of user
    String email;
    double role;

    /**
     * contructor of adapter
     * @param list
     * @param email
     * @param context
     */
    public StaffManageFoodAdapter(ArrayList<Food> list, String email, Context context) {
        this.list = list;
        this.context = context;
        this.email = email;
        this.role=role;
    }

    @NonNull
    @NotNull
    @Override
    public StaffManageFoodAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_menu_item_layout, parent, false);
        return new StaffManageFoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StaffManageFoodAdapter.ViewHolder holder, int position) {
        //get food from list base on position
        Food food = list.get(position);
        //set name of food
        holder.foodName.setText(food.getFoodName());
        //set image of food
        Picasso.get().load(food.getFoodImage()).into(holder.foodImage);
        /*
        Delete food event
         */
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("food").document(list.get(position).getFoodId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               //notify to user
                                Toast.makeText(v.getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                //Remove food
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());
                                //notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Delete fail", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        /*
        event for click item
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start intent to UpdateFoodAcivity
                Intent intent = new Intent(context, UpdateFoodActivity.class);
                intent.putExtra("ID", list.get(position).getFoodId());
                intent.putExtra("USER_EMAIL", email);
                intent.putExtra("ROLE", role);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ArrayFilter(ArrayList<Food> newlist) {
        this.list = newlist;
        notifyDataSetChanged();
    }

    /**
     * Customer class ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*
        Component of ViewHolder
         */
        public ImageView foodImage;
        public TextView foodName;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            /*
            Find view by ID for all component in need
             */
            foodImage = itemView.findViewById(R.id.staff_menu_item_image);
            foodName = itemView.findViewById(R.id.staff_menu_item_text);
            btnDelete = itemView.findViewById(R.id.staff_menu_item_btnDelete);
        }
    }
}

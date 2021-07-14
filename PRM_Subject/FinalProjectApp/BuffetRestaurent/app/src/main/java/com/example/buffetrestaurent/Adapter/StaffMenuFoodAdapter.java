package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StaffMenuFoodAdapter extends RecyclerView.Adapter<StaffMenuFoodAdapter.MyViewHolder> {
    private List<Food> foodsList;
    private List<String> listMenuDetailId;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFoodName;
        ImageView imgFoodImage;
        Button btnDelete;
        MyViewHolder(View view) {
            super(view);
            txtFoodName = view.findViewById(R.id.staff_menu_item_text);
            imgFoodImage = view.findViewById(R.id.staff_menu_item_image);
            btnDelete = view.findViewById(R.id.staff_menu_item_btnDelete);
        }
    }
    public StaffMenuFoodAdapter(List<Food> foodsList, List<String> listMenuDetailId, Context context) {
        this.foodsList = foodsList;
        this.listMenuDetailId = listMenuDetailId;
        this.context= context;
    }
    @NonNull
    @Override
    public StaffMenuFoodAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_menu_item_layout, parent, false);
        return new StaffMenuFoodAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(StaffMenuFoodAdapter.MyViewHolder holder, int position) {
        Food food = foodsList.get(position);
        holder.txtFoodName.setText(food.getFoodName());

        Picasso.get().load(food.getFoodImage()).into(holder.imgFoodImage);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("menu_detail").document(listMenuDetailId.get(position))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                foodsList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, foodsList.size());
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
    }
    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}

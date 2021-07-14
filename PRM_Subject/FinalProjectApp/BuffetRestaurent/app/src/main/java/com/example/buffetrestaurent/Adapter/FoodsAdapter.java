package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.MyViewHolder> {
    private List<Food> foodsList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFoodName;
        ImageView imgFoodImage;
        MyViewHolder(View view) {
            super(view);
            txtFoodName = view.findViewById(R.id.listfood_txtFoodName);
            imgFoodImage = view.findViewById(R.id.listfood_imgFoodImage);
        }
    }
    public FoodsAdapter(List<Food> foodsList, Context context) {
        this.foodsList = foodsList;
        this.context= context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listfood_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Food food = foodsList.get(position);
        holder.txtFoodName.setText(food.getFoodName());

        Picasso.get().load(food.getFoodImage()).into(holder.imgFoodImage);
    }
    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}

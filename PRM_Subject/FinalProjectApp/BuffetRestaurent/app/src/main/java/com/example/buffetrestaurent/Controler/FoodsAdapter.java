package com.example.buffetrestaurent.Controler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;

import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.MyViewHolder> {
    private List<Food> foodsList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFoodName;
        MyViewHolder(View view) {
            super(view);
            txtFoodName = view.findViewById(R.id.listfood_txtFoodName);
        }
    }
    public FoodsAdapter(List<Food> foodsList) {
        this.foodsList = foodsList;
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
    }
    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}

package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;

import org.jetbrains.annotations.NotNull;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AddFoodToMenuAdapter extends RecyclerView.Adapter<AddFoodToMenuAdapter.ViewHolder> {
    private ArrayList <Food> listFood;
    private Context context;

    public  AddFoodToMenuAdapter(Context context,ArrayList <Food> listFood){
        this.listFood = listFood;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmenu_layout, parent,false);
        return new  AddFoodToMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddFoodToMenuAdapter.ViewHolder holder, int position) {
        Food food = listFood.get(position);
        Picasso.get().load(food.getFoodImage()).into(holder.imageFood);
        holder.nameFood.setText(food.getFoodName());
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(food.isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                food.setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageFood;
        public TextView nameFood;
        public CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            imageFood = itemView.findViewById(R.id.addmenu_image);
            nameFood = itemView.findViewById(R.id.addmenu_name);
            checkBox = itemView.findViewById(R.id.addmenu_checkbox);
        }
    }
}

package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyDiscountListAdapter extends RecyclerView.Adapter<BuyDiscountListAdapter.MyViewHolder> {
    private List<Discount> discountsList;
    private List<String> listMenuDetailId;
    private Context context;
    private OnBtnBuyClickListener mBtnBuyClick;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDiscountName;
        TextView txtDiscountPercent;
        TextView txtDiscountPoint;
        Button btnBuy;
        MyViewHolder(View view, OnBtnBuyClickListener mbtnBuyClick) {
            super(view);
            txtDiscountName = view.findViewById(R.id.customer_discount_list_txtDiscountName);
            txtDiscountPercent = view.findViewById(R.id.customer_discount_list_txtDiscountPercent);
            txtDiscountPoint = view.findViewById(R.id.customer_discount_list_txtDiscountPoint);
            btnBuy = view.findViewById(R.id.customer_discount_list_btnBuy);

            btnBuy.setOnClickListener((v) -> {
                if(mbtnBuyClick != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mbtnBuyClick.onBtnBuyClick(position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
    public BuyDiscountListAdapter(List<Discount> discountsList, OnBtnBuyClickListener mBtnBuyClick,Context context) {
        this.discountsList = discountsList;
        this.context= context;
        this.mBtnBuyClick = mBtnBuyClick;
    }
    @NonNull
    @Override
    public BuyDiscountListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discount_list_layout, parent, false);
        return new BuyDiscountListAdapter.MyViewHolder(itemView,mBtnBuyClick);
    }
    @Override
    public void onBindViewHolder(BuyDiscountListAdapter.MyViewHolder holder, int position) {
        Discount discount = discountsList.get(position);
        holder.txtDiscountName.setText(discount.getDiscountName());
        holder.txtDiscountPercent.setText(String.valueOf(discount.getDiscountPercent())+" %");
        holder.txtDiscountPoint.setText(String.valueOf(discount.getDiscountPoint())+" points");

//        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("menu_detail").document(listMenuDetailId.get(position))
//                        .delete()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(v.getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
////                                foodsList.remove(position);
////                                notifyItemRemoved(position);
////                                notifyItemRangeChanged(position, foodsList.size());
////                                //notifyDataSetChanged();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(v.getContext(), "Delete fail", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return discountsList.size();
    }

    public interface OnBtnBuyClickListener{
        void onBtnBuyClick(int position);
    }
}
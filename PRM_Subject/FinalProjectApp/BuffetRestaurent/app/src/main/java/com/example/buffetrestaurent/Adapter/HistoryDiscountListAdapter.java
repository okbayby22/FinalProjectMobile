package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;

import java.util.List;

public class HistoryDiscountListAdapter extends RecyclerView.Adapter<HistoryDiscountListAdapter.MyViewHolder> {
    private List<Discount> discountsList;
    private Context context;
    private HistoryDiscountListAdapter.OnBtnCopyClickListener mBtnCopyClick;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDiscountName;
        TextView txtDiscountPercent;
        TextView txtDiscountCode;
        Button btnCopy;
        MyViewHolder(View view, HistoryDiscountListAdapter.OnBtnCopyClickListener mbtnCopyClick) {
            super(view);
            txtDiscountName = view.findViewById(R.id.customer_discount_history_list_txtDiscountName);
            txtDiscountPercent = view.findViewById(R.id.customer_discount_history_list_txtDiscountPercent);
            txtDiscountCode = view.findViewById(R.id.customer_discount_history_list_txtDiscountCode);
            btnCopy = view.findViewById(R.id.customer_discount_history_list_btnCopy);
            //btnCopy.setText("Copy");
            btnCopy.setOnClickListener((v) -> {
                if(mbtnCopyClick != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mbtnCopyClick.onBtnCopyClick(position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
    public HistoryDiscountListAdapter(List<Discount> discountsList, HistoryDiscountListAdapter.OnBtnCopyClickListener mBtnCopyClick, Context context) {
        this.discountsList = discountsList;
        this.context= context;
        this.mBtnCopyClick = mBtnCopyClick;
    }
    @NonNull
    @Override
    public HistoryDiscountListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discount_history_list_layout, parent, false);
        return new HistoryDiscountListAdapter.MyViewHolder(itemView,mBtnCopyClick);
    }
    @Override
    public void onBindViewHolder(HistoryDiscountListAdapter.MyViewHolder holder, int position) {
        //Discount discount = discountsList.get(position);
        holder.txtDiscountCode.setText("Code: "+discountsList.get(position).getDiscountId());
        holder.txtDiscountName.setText(discountsList.get(position).getDiscountName());
        holder.txtDiscountPercent.setText(discountsList.get(position).getDiscountPercent()+" %");
    }
    @Override
    public int getItemCount() {
        return discountsList.size();
    }

    public interface OnBtnCopyClickListener{
        void onBtnCopyClick(int position);
    }
}

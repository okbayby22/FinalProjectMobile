package com.example.buffetrestaurent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Controller.Activity.EditDiscountStaffManage;
import com.example.buffetrestaurent.Controller.Activity.HomePageStaff;
import com.example.buffetrestaurent.Controller.Activity.StaffDiscountManagement;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;


public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.MyViewHolder>{
    private ArrayList<Discount> discountList;
    public Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public int viewTypeMain;
    public DiscountAdapter(Context context,ArrayList<Discount> discountList){
        this.context = context;
        this.discountList = discountList;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listdiscount_layout, parent, false);
            return new MyViewHolder(itemView,viewType);
        }else if(viewType == TYPE_HEADER){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.headerdiscount_layout, parent, false);
            return new MyViewHolder(itemView,viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DiscountAdapter.MyViewHolder holder, int position) {

           if(viewTypeMain == TYPE_ITEM){
               Discount discount = discountList.get(position-1);
               holder.txtDiscountname.setText(discount.getDiscountName());
               if(discount.getDiscountStatus()==1){
                   holder.txtDiscountStatus.setText("Enable");
                   holder.txtDiscountStatus.setBackgroundResource(R.color.success);
                   holder.txtDiscountStatus.setTextColor(Color.WHITE);
                  // holder.txtDiscountStatus.setError("Error");
               }else if(discount.getDiscountStatus()==2){
                   holder.txtDiscountStatus.setText("Disable");
                   holder.txtDiscountStatus.setBackgroundResource(R.color.Red);
                   holder.txtDiscountStatus.setTextColor(Color.WHITE);
               }
               holder.txtDiscountPercent.setText(String.valueOf(discount.getDiscountPercent()));
               holder.txtDiscountPoint.setText(String.valueOf(discount.getDiscountPoint()));
               holder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {
                       if (isLongClick){
                           Toast.makeText(context, "Long Click: " + discount.getDiscountName(), Toast.LENGTH_SHORT).show();
                            }
                       else
                           Toast.makeText(context, "Short Click: " + discount.getDiscountName(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(view.getContext(), EditDiscountStaffManage.class);
                            intent.putExtra("Discount_ID", discount.getDiscountId());
                            view.getContext().startActivity(intent);
                   }
               });
           }
    }
    @Override
    public int getItemCount() {
        return discountList.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDiscountname,txtDiscountPoint,txtDiscountPercent,txtDiscountStatus;
        private ItemClickListener  itemClickListener;
        MyViewHolder(View view,int viewType) {
            super(view);
            view.setOnClickListener(this);
            if(viewType==TYPE_ITEM){
                txtDiscountname = view.findViewById(R.id.listdiscount_nameDiscount);
                txtDiscountPoint = view.findViewById(R.id.listdiscount_Point);
                txtDiscountPercent = view.findViewById(R.id.listdiscount_Percent);
                txtDiscountStatus = view.findViewById(R.id.listdiscount_status);
                viewTypeMain =1;
            }else if(viewType==TYPE_HEADER){
                viewTypeMain =0;
            }

        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }
}

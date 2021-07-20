package com.example.buffetrestaurent.Adapter;

import android.app.Activity;
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
    //Contain all discount object
    private ArrayList<Discount> discountList;
    //context of the activity call to adapter
    public Context context;
    //Type to state that is header of list
    private static final int TYPE_HEADER = 0;
    //Type to state that is item of list
    private static final int TYPE_ITEM = 1;
    //Contain type of item
    public int viewTypeMain;
    //Contain UserEmail of staff is using app
    String userEmail;
    //Constructor of adapter
    double role;

    public DiscountAdapter(Context context,ArrayList<Discount> discountList,String userEmail,double role){
        this.context = context;
        this.discountList = discountList;
        this.userEmail = userEmail;
        this.role=role;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Check type is item or not
        if(viewType == TYPE_ITEM){
            //Inflate the layout of item
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listdiscount_layout, parent, false);
            return new MyViewHolder(itemView,viewType);
        //If not item so it would be header
        }else if(viewType == TYPE_HEADER){
            //Inflate layout of header
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.headerdiscount_layout, parent, false);
            return new MyViewHolder(itemView,viewType);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull DiscountAdapter.MyViewHolder holder, int position) {
           //Check type is item or not
           if(viewTypeMain == TYPE_ITEM){
               //Get object from list discount
               Discount discount = discountList.get(position-1);
               //set name
               holder.txtDiscountname.setText(discount.getDiscountName());
               //Check status to set it enable or disable
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
               //Set percent
               holder.txtDiscountPercent.setText(String.valueOf(discount.getDiscountPercent()));
               //set point
               holder.txtDiscountPoint.setText(String.valueOf(discount.getDiscountPoint()));
               //Create event click item
               holder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {
                       //Check whether is long click
                       if (isLongClick){
                            }
                       else{
                           //Create Intent to edit discount
                           Intent intent=new Intent(view.getContext(), EditDiscountStaffManage.class);
                           //Send ID of discount
                           intent.putExtra("Discount_ID", discount.getDiscountId());
                           intent.putExtra("ROLE", role);
                           //Send user email
                           intent.putExtra("USER_EMAIL", userEmail);
                           view.getContext().startActivity(intent);
                       }
                   }
               });
           }
    }
    @Override
    public int getItemCount() {
        return discountList.size()+1;//Size of list, plus 1 for header type
    }
    //Create class view for adapter
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Text View
        TextView txtDiscountname,txtDiscountPoint,txtDiscountPercent,txtDiscountStatus;
        //Item click for click event
        private ItemClickListener  itemClickListener;
        //Custom view holder
        MyViewHolder(View view,int viewType) {
            super(view);
            //sest click listener
            view.setOnClickListener(this);
            //Check whether is type item
            if(viewType==TYPE_ITEM){
                txtDiscountname = view.findViewById(R.id.listdiscount_nameDiscount);
                txtDiscountPoint = view.findViewById(R.id.listdiscount_Point);
                txtDiscountPercent = view.findViewById(R.id.listdiscount_Percent);
                txtDiscountStatus = view.findViewById(R.id.listdiscount_status);
                //set type of that item
                viewTypeMain =1;
            //Type is header
            }else if(viewType==TYPE_HEADER){
                //set type of that item
                viewTypeMain =0;
            }

        }
        //Create event of click listener
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        //Create event of click listener
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
    //Create inteface of click listener
    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
    //Get item view type to get what type of that item
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

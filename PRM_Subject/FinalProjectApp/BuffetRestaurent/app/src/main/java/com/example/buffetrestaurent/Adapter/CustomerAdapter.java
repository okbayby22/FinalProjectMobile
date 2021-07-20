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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Controller.Activity.CustomerProfile;
import com.example.buffetrestaurent.Controller.Activity.StaffProfile;
import com.example.buffetrestaurent.Controller.Activity.UserProfile;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{
    private ArrayList<Customer> list;
    private Context context;
    double role;

    public CustomerAdapter(ArrayList<Customer> list, Context context,double role) {
        this.list = list;
        this.context = context;
        this.role=role;
    }

    @NonNull
    @NotNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new CustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerAdapter.ViewHolder holder, int position) {
        Customer Staff = list.get(position);
        if(Staff.getCustomerName().equals("")){
            holder.nameCus.setText("None");
        }else{
            holder.nameCus.setText(Staff.getCustomerName().toString());
        }
        holder.phoneCus.setText(Staff.getCustomerPhone());
        GradientDrawable shape = new GradientDrawable();
        if (Staff.getCustomerStatus() == 1) {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.GREEN);
            shape.setCornerRadius(20);
            holder.statusCus.setBackgroundDrawable(shape);
            holder.statusCus.setTextColor(Color.BLACK);
            holder.statusCus.setText("ENABLE");
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.RED);
            shape.setCornerRadius(20);
            holder.statusCus.setBackgroundDrawable(shape);
            holder.statusCus.setTextColor(Color.WHITE);
            holder.statusCus.setText("DISABLE");
        }

        holder.statusCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (Staff.getCustomerStatus() == 1) {
                        new AlertDialog.Builder(context).setTitle("Disable Staff Notice").setMessage("Confirm Disable This Staff")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Customer staff = list.get(position);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> updateData = new HashMap<>();
                                        updateData.put("staffStatus", 0);
                                        db.collection("staffs")
                                                .document(list.get(position).getCustomerId())
                                                .update(updateData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        staff.setCustomerStatus(0);
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                    }
                                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyDataSetChanged();
                            }
                        }).show();
                    } else if (Staff.getCustomerStatus() == 0) {
                        new AlertDialog.Builder(context).setTitle("Enable Staff Notice").setMessage("Confirm Enable This Staff")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Customer staff = list.get(position);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> updateData = new HashMap<>();
                                        updateData.put("staffStatus", 1);
                                        db.collection("staffs")
                                                .document(list.get(position).getCustomerId())
                                                .update(updateData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        staff.setCustomerStatus(1);
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                    }
                                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyDataSetChanged();
                            }
                        }).show();
                    }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerProfile.class);
                intent.putExtra("ID", list.get(position).getCustomerId());
                intent.putExtra("EMAIL", list.get(position).getCustomerEmail());
                intent.putExtra("ROLE", role);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ArrayFilter(ArrayList<Customer> newlist) {
        this.list = newlist;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageCus;
        public TextView nameCus;
        public TextView statusCus;
        public TextView phoneCus;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCus = itemView.findViewById(R.id.user_item_icon);
            nameCus = itemView.findViewById(R.id.user_item_txtName);
            phoneCus = itemView.findViewById(R.id.user_item_txtPhone);
            statusCus = itemView.findViewById(R.id.user_item_txtStatus);
        }
    }
}

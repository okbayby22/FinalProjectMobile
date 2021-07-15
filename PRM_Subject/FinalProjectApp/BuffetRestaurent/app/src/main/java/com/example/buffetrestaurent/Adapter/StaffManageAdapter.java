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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Controller.Activity.StaffManageActivity;
import com.example.buffetrestaurent.Controller.Activity.StaffProfile;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.Model.Reservation;
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

public class StaffManageAdapter extends RecyclerView.Adapter<StaffManageAdapter.ViewHolder> {

    private ArrayList<Staff> list;
    private Context context;

    public StaffManageAdapter(ArrayList<Staff> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public StaffManageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new StaffManageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StaffManageAdapter.ViewHolder holder, int position) {
        Staff Staff = list.get(position);
        holder.nameStaff.setText(Staff.getStaffName());
        Picasso.get().load(Staff.getStaffImage()).into(holder.imageStaff);
        if (Staff.getStaffRole() == 1) {
            holder.roleStaff.setTextSize(15);
            holder.roleStaff.setText("Staff");
        }else if(Staff.getStaffRole() ==  2){
            holder.roleStaff.setTextSize(15);
            holder.roleStaff.setText("Manager");
        }
        holder.phoneStaff.setText(Staff.getStaffPhone());
        GradientDrawable shape = new GradientDrawable();
        if (Staff.getStaffStatus() == 1) {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.GREEN);
            shape.setCornerRadius(20);
            holder.statusStaff.setBackgroundDrawable(shape);
            holder.statusStaff.setTextColor(Color.BLACK);
            holder.statusStaff.setText("ENABLE");
        } else {
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.RED);
            shape.setCornerRadius(20);
            holder.statusStaff.setBackgroundDrawable(shape);
            holder.statusStaff.setTextColor(Color.WHITE);
            holder.statusStaff.setText("DISABLE");
        }

        holder.statusStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Staff.getStaffRole() == 2) {
                    new AlertDialog.Builder(context).setTitle("Update Staff Notice").setMessage("Can enable/disable this staff")
                            .show();
                } else {
                    if (Staff.getStaffStatus() == 1) {
                        new AlertDialog.Builder(context).setTitle("Disable Staff Notice").setMessage("Confirm Disable This Staff")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Staff staff = list.get(position);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> updateData = new HashMap<>();
                                        updateData.put("staffStatus", 0);
                                        db.collection("staffs")
                                                .document(list.get(position).getStaffId())
                                                .update(updateData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        staff.setStaffStatus(0);
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
                    } else if (Staff.getStaffStatus() == 0) {
                        new AlertDialog.Builder(context).setTitle("Enable Staff Notice").setMessage("Confirm Enable This Staff")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Staff staff = list.get(position);
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> updateData = new HashMap<>();
                                        updateData.put("staffStatus", 1);
                                        db.collection("staffs")
                                                .document(list.get(position).getStaffId())
                                                .update(updateData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        staff.setStaffStatus(1);
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
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StaffProfile.class);
                intent.putExtra("ID", list.get(position).getStaffId());
                intent.putExtra("EMAIL", list.get(position).getStaffEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ArrayFilter(ArrayList<Staff> newlist) {
        this.list = newlist;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageStaff;
        public TextView nameStaff;
        public TextView roleStaff;
        public TextView statusStaff;
        public TextView phoneStaff;

        public ViewHolder(View itemView) {
            super(itemView);
            imageStaff = itemView.findViewById(R.id.staff_item_icon);
            nameStaff = itemView.findViewById(R.id.staff_item_txtName);
            roleStaff = itemView.findViewById(R.id.staff_item_txtRole);
            phoneStaff = itemView.findViewById(R.id.staff_item_txtPhone);
            statusStaff = itemView.findViewById(R.id.staff_item_txtStatus);
        }
    }
}

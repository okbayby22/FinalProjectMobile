package com.example.buffetrestaurent.Controler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Desk;
import com.example.buffetrestaurent.R;

import java.util.List;


public class DeskAdapter extends RecyclerView.Adapter<DeskAdapter.MyViewHolder> {

    private List<Desk> desklist;
    public Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTablename;
        MyViewHolder(View view) {
            super(view);
            txtTablename = view.findViewById(R.id.tableName);
        }
    }
    public DeskAdapter(Context context, List<Desk> deskList) {
        this.desklist = deskList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(DeskAdapter.MyViewHolder holder, int position) {
        Desk desk = desklist.get(position);
        holder.txtTablename.setText("Table "+String.valueOf(desk.getDesk_id()));
    }
    @Override
    public int getItemCount() {
        return desklist.size();
    }
}
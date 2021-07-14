package com.example.buffetrestaurent.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>{

    private ArrayList<Reservation> list;
    private final Context context;

    public ReservationAdapter(ArrayList<Reservation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lecturerView = inflater.inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(lecturerView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder( ReservationAdapter.ViewHolder holder, int position) {
        Reservation reservation = list.get(position);
        holder.date.setText("Date: "+reservation.getReservationDate().toString());
        holder.ticket.setText("Number of tickets: "+reservation.getNumberTickets()+"");
        holder.price.setText(String.valueOf("Total: "+reservation.getReservationAmount()));
        holder.time.setText("Time: "+reservation.getReservationTime());
        if(reservation.getReservationStatus() == 1){
            holder.status.setText("Success");
        }else if(reservation.getReservationStatus() == 2){
            holder.status.setText("Cancel");
        }else{
            holder.status.setText("Pending.....");
        }
        if(holder.status.getText().equals("Success")){
            holder.status.setTextColor(Color.GREEN);
        }else if(holder.status.getText().equals("Cancel")){
            holder.status.setTextColor(Color.RED);
        }else if(holder.status.getText().equals("Pending.....")){
            holder.status.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Reservation getReservationAt(int position){
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avt;
        public TextView date;
        public TextView ticket;
        public TextView price;
        public TextView time;
        public TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.CancelReservation_Date);
            avt = itemView.findViewById(R.id.CancelReservation_Img);
            ticket = itemView.findViewById(R.id.CancelReservation_NumOfTicket);
            price = itemView.findViewById(R.id.CancelReservation_Price);
            time = itemView.findViewById(R.id.CancelReservation_Time);
            status = itemView.findViewById(R.id.CancelReservation_Status);

        }
    }

    public void ArrayFilter(ArrayList<Reservation> newlist){
        this.list = newlist;
        notifyDataSetChanged();
    }
}

package com.example.buffetrestaurent.Adapter;

import android.content.Context;
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

    @Override
    public void onBindViewHolder( ReservationAdapter.ViewHolder holder, int position) {
        Reservation reservation = list.get(position);
        holder.date.setText(reservation.getReservationDate().toString() +" "+ reservation.getReservationTime());
        holder.ticket.setText("Number of tickets: "+reservation.getNumberTickets()+"");
        holder.price.setText(String.valueOf("Total: "+reservation.getReservationAmount()));
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

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.CancelReservation_Date);
            avt = itemView.findViewById(R.id.CancelReservation_Img);
            ticket = itemView.findViewById(R.id.CancelReservation_NumOfTicket);
            price = itemView.findViewById(R.id.CancelReservation_Price);

        }
    }

    public void ArrayFilter(ArrayList<Reservation> newlist){
        this.list = newlist;
        notifyDataSetChanged();
    }
}

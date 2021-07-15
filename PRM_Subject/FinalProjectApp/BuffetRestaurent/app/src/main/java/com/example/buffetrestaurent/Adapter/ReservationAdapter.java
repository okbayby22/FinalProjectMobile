package com.example.buffetrestaurent.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Controller.Activity.UpdateReservation;
import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Reservation;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>{

    private ArrayList<Reservation> list;
    private final Context context;
    String email;

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
        holder.item_Reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = list.get(position).getReservationStatus();
                if(check==1 || check ==2){
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("ERROR")
                            .setMessage("Cannot update this reservation")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                }else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("customers")
                            .whereEqualTo("customerId",list.get(position).getCustomerId() )
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@android.support.annotation.NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Customer customerInfor = document.toObject(Customer.class);
                                            email = customerInfor.getCustomerEmail();
                                        }
                                    } else {

                                    }
                                }
                            });
                    System.out.println("//////////////////"+email);
                    Intent intent = new Intent(context, UpdateReservation.class);
                    intent.putExtra("GETID",list.get(position).getReservationId());
                    Reservation res = list.get(position);
                    System.out.println(res);
                    intent.putExtra("GETRESERVATION", res);
                    intent.putExtra("USER_EMAIL", email);
                    context.startActivity(intent);
                }
            }
        });
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
        public LinearLayout item_Reservation;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.CancelReservation_Date);
            avt = itemView.findViewById(R.id.CancelReservation_Img);
            ticket = itemView.findViewById(R.id.CancelReservation_NumOfTicket);
            price = itemView.findViewById(R.id.CancelReservation_Price);
            time = itemView.findViewById(R.id.CancelReservation_Time);
            status = itemView.findViewById(R.id.CancelReservation_Status);
            item_Reservation = itemView.findViewById(R.id.item_Reservation);
            email = ((Activity) context).getIntent().getStringExtra("USER_EMAIL");
        }
    }

    public void ArrayFilter(ArrayList<Reservation> newlist){
        this.list = newlist;
        notifyDataSetChanged();
    }
}

package com.example.buffetrestaurent.Controler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buffetrestaurent.Model.Desk;
import com.example.buffetrestaurent.R;

import java.util.ArrayList;


public class DeskAdapter extends RecyclerView.Adapter<DeskAdapter.ViewHolder> {

    ArrayList<Desk> desksList;


    Context context;

    /**
     * Constructor method Teacher adapter
     *
     * @param desksList: stores the teacher list
     * @param context      : stores the context
     */
    public DeskAdapter(ArrayList desksList, Context context) {
        this.desksList = desksList;
        this.context = context;
    }
    /**
     * Method onBindViewHolder : move the data into the ViewHolder
     *
     * @param holder   :stores the ViewHolder
     * @param position :stores the position of the object when binding data into the view
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Desk desk = desksList.get(position);
        holder.deskName.setText(desk.getDeskId());
     //   new ImageURL(holder.itemView.findViewById(R.id.imageView2)).execute(teacher.getUrlImage());
    }

    /**
     * Method onCreateViewHolder : create ViewHolder object and store the view for showing data
     *
     * @param parent    : stores the parent group containing all views inside
     * @param viewType: stores the type of view
     * @return the new ViewHolder after inflating a new view based on xml file
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //View teacherView = inflater.inflate(R.layout.item_layout, parent, false);
        View teacherView = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(teacherView);
    }



    /**
     * Method getItemCounts:  get the size of the list
     *
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return desksList.size();
    }

    /**
     * Create class ViewHolder to keep the View structure
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Store the teacher's image
        public ImageView deskImage;

        //Store the teacher's name
        public TextView deskName;

        /**
         * Create constructor method ViewHolder
         *
         * @param itemView : stores the view of the activity
         */
        public ViewHolder(View itemView) {
            super(itemView);
            //deskImage = itemView.findViewById(R.id.de);
            deskName = itemView.findViewById(R.id.tableName);
        }
    }
}
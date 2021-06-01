package com.example.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assignment2.Model.Teacher;
import com.example.assignment2.Model.DownLoadImageTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/*
 * Student's name: Ngo Hoan Tam Huy
 * Student's code: CE140548
 * Class:          SE1401
 */
public class ListTeacher extends BaseAdapter{

    Context context; // Activity context
    ArrayList<Teacher> listTeacher; // List of teacher

    public ListTeacher(Context context, ArrayList<Teacher> listTeacher){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.listTeacher = listTeacher;
    }
    @Override
    public int getCount() {
        return listTeacher.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Method set contain for each row of list view
     * @return row view of list view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_list_layout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtTeacherName);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgTeacherImage);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(listTeacher.get(position).getTeacherName());
        new DownLoadImageTask(viewHolder.img).execute(listTeacher.get(position).getImageURL());

        return result;
    }

    /**
     * Class containt View element
     */
    private static class ViewHolder {

        TextView txtName;
        ImageView img;

    }

}

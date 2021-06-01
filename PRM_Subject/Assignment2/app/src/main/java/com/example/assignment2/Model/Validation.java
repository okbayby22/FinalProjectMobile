package com.example.assignment2.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.assignment2.R;
import com.example.assignment2.activity_add;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Validation {
    /**
     * Method check duplicate teacher id show error dialog if ID is duplicate
     * @param listTeacher list teacher
     * @param teacherID teacher ID need to check
     * @param context Activity context
     * @return False if ID is duplicate
     */
    public boolean CheckDuplicateID(ArrayList<Teacher> listTeacher, String teacherID, Context context){
        boolean check = true;
        for(Teacher i : listTeacher){
            if(i.getTeacherID().equalsIgnoreCase(teacherID)){
                check = false;
                AlertDialog errorDialog=new AlertDialog.Builder(context)
                        .setTitle(R.string.errTitleDuplicate)
                        .setMessage(R.string.errDuplicate)
                        .setNegativeButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                errorDialog.show();
            }
        }
        return check;
    }

    /**
     * Method check empty string show error dialog if string is empty
     * @param view TextView need to check
     * @param context Activity context
     * @return
     */
    public boolean CheckEmpty(TextView view, Context context, int msgTitle, int msg){
        boolean check=true;
        if(view.getText().toString().trim().isEmpty()){
            check = false;
            AlertDialog errorDialog=new AlertDialog.Builder(context)
                    .setTitle(msgTitle)
                    .setMessage(msg)
                    .setNegativeButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            errorDialog.show();
        }
        return check;
    }
    /**
     * Method check image URL  show error dialog if cannot load image from URK
     * @param imgURL TextView need to check
     * @param context Activity context
     * @return
     */
    public boolean CheckURL(TextView imgURL, Context context){
        boolean check=true;
        InputStream is = null;
        try {
            is = new URL(imgURL.getText().toString()).openStream();
        }
        catch (IOException e) {
            e.printStackTrace();
            check = false;
            imgURL.setText("https://i.ibb.co/rmRpF3X/Avatar.jpg");
            AlertDialog errorDialog=new AlertDialog.Builder(context)
                    .setTitle(R.string.errTitleURL)
                    .setMessage(R.string.errURL)
                    .setNegativeButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            errorDialog.show();
        }
        return check;
    }
}

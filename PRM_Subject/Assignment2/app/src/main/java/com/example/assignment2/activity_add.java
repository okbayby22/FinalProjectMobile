package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.Model.DAO;
import com.example.assignment2.Model.Teacher;
import com.example.assignment2.Model.Validation;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Student's name: Ngo Hoan Tam Huy
 * Student's code: CE140548
 * Class:          SE1401
 */
public class activity_add extends AppCompatActivity {

    TextView txtName;  // teacher name on view
    TextView imgURL;  // image URL on view
    TextView txtID; // teacher ID on view
    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dao = new DAO(this);
    }

    /**
     * Event when user click Cancel button
     * @param view
     */
    public void btnCancel(View view){
        this.finish();
    }

    /**
     * Event when user click OK button
     * @param view
     */
    public void btnOK(View view){
        txtName = (TextView) findViewById(R.id.txtTeacherName_Add);
        txtID = (TextView) findViewById(R.id.txtTeacherID_Add);
        imgURL = (TextView) findViewById(R.id.txtImageURL);
        Validation check=new Validation();

        //check all input
        if(check.CheckEmpty(txtID, activity_add.this, R.string.errTitleEmptyID, R.string.errEmptyID)){ // check teacher ID can not empty
            if(check.CheckDuplicateID(dao.loadData(), txtID.getText().toString(), activity_add.this)){ // check teacher ID can not duplicate
                if(check.CheckEmpty(txtName, activity_add.this, R.string.errTitleEmpty, R.string.errEmpty)){ // check teacher name can not empty
                    //int checkURL= check.CheckURL(imgURL,activity_add.this);
                    // Add new element
                    Teacher newTeacher=new Teacher(txtName.getText().toString(),imgURL.getText().toString(),txtID.getText().toString());

                    boolean result = dao.InsertData(newTeacher);
                    if(result){
                        Toast.makeText(this,"Data Inserted To Sqlite Database",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                    }

                    // Send data to main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                    this.finish();
                }
            }
        }

    }
}
package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.Model.DAO;
import com.example.assignment2.Model.DownLoadImageTask;
import com.example.assignment2.Model.Teacher;
import com.example.assignment2.Model.Validation;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {

    String updateTeacherID;
    Teacher updateTeacher;
    TextView txtName; // teacher name get from view
    TextView imgURL; // image url get from view
    TextView txtID; // teacher id get from view
    ImageView teacherImg; // teacher image get from view
    Button btnOK,btnCancel; // button ok, button cancel
    int index; // index of Update element
    DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        dao =new DAO(this);

        // connect to View for element
        txtName = findViewById(R.id.txtTeacherName_Update);
        txtID = findViewById(R.id.txtTeacherID_Update);
        imgURL = findViewById(R.id.txtImageURL_Update);
        teacherImg =findViewById(R.id.imgTeacher);
        btnOK = findViewById(R.id.btnOK_Update);
        btnCancel = findViewById(R.id.btnCancel_Update);

        // get teacher ID of update teacher
        updateTeacherID = getIntent().getStringExtra("TEACHERID");
        updateTeacher = dao.loadSingleData(updateTeacherID);

        // set data for View
        txtName.setText(updateTeacher.getTeacherName());
        txtID.setText(updateTeacher.getTeacherID());
        imgURL.setText(updateTeacher.getImageURL());
        new DownLoadImageTask(teacherImg).execute(updateTeacher.getImageURL());

        // set event when user click Ok button
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation check=new Validation();
                if(check.CheckEmpty(txtName, UpdateActivity.this, R.string.errTitleEmpty, R.string.errEmpty)){ // check teacher name can not empty
                    // Update element
                    updateTeacher.setTeacherName(txtName.getText().toString());
                    updateTeacher.setImageURL(imgURL.getText().toString());

                    boolean result = dao.UpdateData(updateTeacher);

                    if(result){
                        Toast.makeText(v.getContext(),"Data update successful",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(v.getContext(),"Error",Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        });

        // set event when user click cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
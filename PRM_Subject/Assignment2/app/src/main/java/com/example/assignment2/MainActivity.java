package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import android.os.Bundle;

import com.example.assignment2.Model.DAO;
import com.example.assignment2.Model.Teacher;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Student's name: Ngo Hoan Tam Huy
 * Student's code: CE140548
 * Class:          SE1401
 */
public class MainActivity extends AppCompatActivity {
    ArrayList<Teacher> listTeacher;
    //Variable save list view get from View
    ListView lView;

    //List teacher to show on view
    ListTeacher lTeacher;

    // create object to connect to database
    DAO dao;

    Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao=new DAO(this);
        activity=this;
        listTeacher=new ArrayList<>();
        listTeacher = dao.loadData();

        //get list view by id
        lView = (ListView) findViewById(R.id.TeacherList);

        //create list teacher
        lTeacher = new ListTeacher(MainActivity.this, listTeacher);

        //set contain for list view
        lView.setAdapter(lTeacher);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.delete_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_option: // if user choose delete in menu
                                // Delete teacher base on teacher ID
                                boolean result=dao.deleteData(listTeacher.get(position).getTeacherID());
                                if(result){
                                    Toast.makeText(MainActivity.this,"Data delete successful",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                                }
                                finish();
                                startActivity(getIntent());
                                break;
                            case R.id.edit_option:
                                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                                intent.putExtra("TEACHERID",listTeacher.get(position).getTeacherID());
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }


    /**
     * Event when click button Add
     * @param view storing the view of the current activity
     */
    public void btnAdd(View view){
        Intent intent = new Intent(this, activity_add.class);
        startActivity(intent);
        finish();
    }
}
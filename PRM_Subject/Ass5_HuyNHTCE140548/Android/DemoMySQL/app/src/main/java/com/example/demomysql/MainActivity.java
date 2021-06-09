package com.example.demomysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.demomysql.Model.Student;
import com.example.demomysql.Utils.Apis;
import com.example.demomysql.Utils.StudentService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    StudentService studentService;
    List<Student> listStudent=new ArrayList<>();
    TextView txtCountStudents;
    Button btnReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCountStudents=(TextView)findViewById(R.id.txtCountStudents);
        btnReload = (Button)findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPersons();
            }
        });
        listPersons();
    }

    public void listPersons(){
        studentService = Apis.getStudentService();
        Call<List<Student>> call=studentService.getStudent();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if(response.isSuccessful()) {
                    listStudent = response.body();
                    txtCountStudents.setText("There are "+listStudent.size()+" student in database");
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }
}
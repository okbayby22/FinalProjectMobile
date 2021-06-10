package com.example.demomysql.Utils;

import com.example.demomysql.Model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface StudentService {
    @GET("listar")
    Call<List<Student>> getStudent();
}

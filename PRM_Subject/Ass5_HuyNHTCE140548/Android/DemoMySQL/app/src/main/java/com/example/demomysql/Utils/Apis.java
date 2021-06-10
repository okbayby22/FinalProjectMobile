package com.example.demomysql.Utils;

public class Apis {
    public static final String URL_001="http://192.168.0.109:8099/student/";

    public static StudentService getStudentService(){
        return  Cliente.getClient(URL_001).create(StudentService.class);
    }
}

package com.example.assignment2.Model;

import java.io.Serializable;

public class Teacher implements Serializable {
    private String teacherName;
    private String imageURL;
    private String teacherID;

    public Teacher(){

    }

    public Teacher(String teacherName, String imageURL, String techerID) {
        this.teacherName = teacherName;
        this.imageURL = imageURL;
        this.teacherID = techerID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTeacherID(String techerID) {
        this.teacherID = techerID;
    }
}

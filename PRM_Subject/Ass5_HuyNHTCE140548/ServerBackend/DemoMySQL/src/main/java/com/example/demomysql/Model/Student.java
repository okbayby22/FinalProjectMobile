package com.example.demomysql.Model;

import javax.persistence.*;

@Table(name = "student")
@Entity
public class Student {

    public Student(){
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int studentID;
    @Column
    private String studentName;

    public int getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}

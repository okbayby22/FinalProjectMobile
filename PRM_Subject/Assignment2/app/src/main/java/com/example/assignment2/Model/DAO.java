package com.example.assignment2.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DAO extends SQLiteOpenHelper{
    //Data base name
    private static final String DATABASE_NAME="TeacherManagementSystem";

    //creates an object for creating, opening and managing the database.
    public DAO(Context context){
        super(context, DATABASE_NAME, null,1);
    }

    //called only once when database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create teacher infor table
        String query;
        query = "CREATE TABLE IF NOT EXISTS TeacherInfor(TeacherID VARCHAR PRIMARY KEY,TeacherName VARCHAR,ImageURL TEXT);";
        db.execSQL(query);
    }

    //called when database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query ;
        query = "DROP TABLE IF EXISTS TeacherInfor";
        db.execSQL(query);
        onCreate(db);
    }

    /**
     * Method insert new teacher to database
     * @param teacherInfor object teacher
     * @return true if insert success else false
     */
    public Boolean InsertData(Teacher teacherInfor){
        SQLiteDatabase db = this.getWritableDatabase(); //create and/or open a database that will be used for reading and writing
        ContentValues contentValues = new ContentValues(); //This class is used to store a set of values that the ContentResolver can process.
        contentValues.put("TeacherID", teacherInfor.getTeacherID());
        contentValues.put("TeacherName", teacherInfor.getTeacherName());
        contentValues.put("ImageURL", teacherInfor.getImageURL());
        long result = db.insert("TeacherInfor", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method load data from database to array list
     * @return ArrayList data
     */
    public ArrayList<Teacher> loadData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  TeacherInfor",null);
        ArrayList<Teacher> listTeacher = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Teacher teacher = new Teacher();
                teacher.setTeacherID(cursor.getString(cursor.getColumnIndex("TeacherID")));
                teacher.setTeacherName(cursor.getString(cursor.getColumnIndex("TeacherName")));
                teacher.setImageURL(cursor.getString(cursor.getColumnIndex("ImageURL")));
                listTeacher.add(teacher);
            } while (cursor.moveToNext());
        }
        return listTeacher;
    }

    /**
     * Method update data base on teacher ID
     * @param teacherInfor teacher object content value need to update
     * @return true if update success else false
     */
    public Boolean UpdateData(Teacher teacherInfor){
        SQLiteDatabase db = this.getWritableDatabase(); //create and/or open a database that will be used for reading and writing
        ContentValues contentValues = new ContentValues(); //This class is used to store a set of values that the ContentResolver can process.
        contentValues.put("TeacherName", teacherInfor.getTeacherName());
        contentValues.put("ImageURL", teacherInfor.getImageURL());
        long result = db.update("TeacherInfor", contentValues, "TeacherID=?", new String[] {teacherInfor.getTeacherID()});
        if(result == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method load single data base on ID
     * @param teacherID teacher ID of teacher want to load
     * @return Object teacher content data need to load
     */
    public Teacher loadSingleData(String teacherID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  TeacherInfor WHERE TeacherID = ?",new String[]{teacherID});
        if(cursor.moveToFirst()){
            Teacher teacher = new Teacher();
            teacher.setTeacherID(cursor.getString(cursor.getColumnIndex("TeacherID")));
            teacher.setTeacherName(cursor.getString(cursor.getColumnIndex("TeacherName")));
            teacher.setImageURL(cursor.getString(cursor.getColumnIndex("ImageURL")));
            return teacher;
        }
        return null;
    }

    /**
     * Method delete data base on ID
     * @param teacherID teacher ID of teacher need to delete
     * @return true if delete success  else false
     */
    public Boolean deleteData(String teacherID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("TeacherInfor", "TeacherID=?", new String[] {teacherID});
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }
}

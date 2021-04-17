package com.example.saad.dbmsproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDB.db";

//user table
    private static final String USER = "Users";
    private static final String U_ID = "u_id";
    private static final String D_ID = "d_id";
    private static final String U_NAME = "name";
    private static final String U_EMAIL = "email";
    private static final String U_PASS = "pass";

 //department table
    private static final String DEPARTMENT = "Department";
    private static final String D_NAME = "name";


 //Project table
    private static final String PROJECT = "Project";
    private static final String P_ID = "p_id";
    private static final String P_NAME = "name";
    private static final String ABSTRACT = "abstract";
    private static final String URL = "URL";
    private static final String timeStamp= "time";


    //public SQLiteDatabase db;
    private static final String USER_CREATE = " CREATE TABLE " + USER +" ( "
            + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +D_ID + " INTEGER NOT NULL,"
            + U_NAME + " TEXT NOT NULL,"
            + U_EMAIL + " TEXT NOT NULL,"
            + U_PASS + " TEXT NOT NULL, "
            + " FOREIGN KEY (d_id) references Department(d_id)"
            + "); ";
    private static final String DEPARTMENT_CREATE = " CREATE TABLE " + DEPARTMENT +" ( "
            + D_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +D_NAME + " TEXT NOT NULL "
            + "); ";


    // project table
    private static final String PROJECT_CREATE = " CREATE TABLE " + PROJECT +" ( "
            + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +U_ID + " INTEGER NOT NULL, "
            +P_NAME + " TEXT NOT NULL,"
            + ABSTRACT + " TEXT NOT NULL,"
            + URL + " TEXT NOT NULL,"
            + timeStamp + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + " FOREIGN KEY (u_id) references Users(u_id) "
            + "); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");


        db.execSQL(DEPARTMENT_CREATE);


        db.execSQL(USER_CREATE);


        db.execSQL(PROJECT_CREATE);

    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            String query = "DROP TABLE IF EXISTS "+USER;
            db.execSQL(query);
            this.onCreate(db);
    }


    public void newUser(USER u)
    {
        /*SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(D_ID,u.get_did());
        values.put(U_NAME,u.get_un());
        values.put(U_EMAIL,u.get_email());
        values.put(U_PASS,u.get_pass());
        db.insert(USER,null,values);
        db.close();*/


    }


  public Cursor GetMyProjects(String u_id){

        SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query("Project", new String[]{"p_id as _id","u_id","name","abstract", "URL", "time"},
              " u_id = ?", new String[]{u_id}, null, null, null);
        return cursor;
    }

    public Cursor ListProjects(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Project", new String[]{"p_id as _id","name"},
                null, null, null, null, null);
        return cursor;

    }

    public Cursor SearchProjects(String search){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, "Project", new String[] {"p_id as _id, name"}, "name" + " LIKE ?",
                new String[] {"%"+ search + "%" }, null, null, null,
                null);

        /*Cursor cursor = db.query("Project", new String[]{"p_id as _id","name"},
                null, null, null, null, null);*/
        return cursor;

    }


    public PROJECT getProjectbyID(String p_id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Project", new String[]{"p_id as _id","name","abstract", "URL", "time"},
                " p_id = ?", new String[]{p_id}, null, null, null);
        cursor.moveToFirst();
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        final String Abstract = cursor.getString(cursor.getColumnIndex("abstract"));
        final String url = cursor.getString(cursor.getColumnIndex("URL"));
        final String time = cursor.getString(cursor.getColumnIndex("time")) ;
        PROJECT project = new PROJECT();
        project.set_abstract(Abstract);
        project.set_name(name);
        project.set_url(url);
        project.set_time(time);
        db.close();
        cursor.close();
        return  project;
    }

    public void DeleteProjectbyID(String p_id){

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete("Project",
                "p_id = ?",
                new String[] {p_id});

        db.close();


    }


    public void UpdateProjectbyID(String p_id,String title, String Abstract ){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put("name", title);
        newValues.put("abstract", Abstract);
        db.update("Project",
                newValues,
                "p_id = ?",
                new String[] {p_id});
        db.close();


    }


/*
    public List<PROJECT> getAllProjects(){

        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "SELECT * FROM " + PROJECT;
        Cursor cursor = db.query(PROJECT,new String[]{P_ID,U_ID,P_NAME,ABSTRACT,URL,timeStamp}, null,null,null,null,null);
        List<PROJECT> projects = new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PROJECT project = new PROJECT();
                project.set_id(cursor.getInt(cursor.getColumnIndex(P_ID)));
                project.set_uid(cursor.getInt(cursor.getColumnIndex(U_ID)));
                project.set_abstract(cursor.getString(cursor.getColumnIndex(ABSTRACT)));
                project.set_name(cursor.getString(cursor.getColumnIndex(P_NAME)));
                project.set_url(cursor.getString(cursor.getColumnIndex(URL)));
                project.set_time(cursor.getString(cursor.getColumnIndex(timeStamp)));
                projects.add(project);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return projects;

    }*/


    public void newProject(PROJECT p)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(U_ID,p.get_uid());
        values.put(P_NAME,p.get_name());
        values.put(ABSTRACT,p.get_abstract());
        values.put(URL,p.get_url());
        /*values.put(timeStamp, "null" );*/
        db.insert(PROJECT,null,values);
        db.close();
    }

    public String checkPass(String name)
    {   SQLiteDatabase db = this.getReadableDatabase();
        String a,b="asd";

        String query = "SELECT name, pass FROM " + USER;
        Cursor cursor = db.rawQuery(query,null);
        b = "not found";
        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(cursor.getColumnIndex("name"));
                if(a.equals(name)){
                    b = cursor.getString(cursor.getColumnIndex("pass"));
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
       return b;
    }


    public int get_idForUname(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        int uid = 1;
        String name;
        String query = "SELECT name, u_id FROM " + USER;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                name = cursor.getString(cursor.getColumnIndex("name"));

                if(name.equals(uname) ){
                    uid = cursor.getInt(cursor.getColumnIndex("u_id"));
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();

        return uid;


    }

    public void PopulateDepartmentTable (){
        String dnames[]={"Civil Engineering", "Mechanical Engineering", "Chemical Engineering", "Computer Systems Engineering", "Electrical Engineering", "Mining Engineering", "CSIT" };
        String dids[] = {"1","2","3", "4", "5", "6", "7"};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i=0; i<7; i++){
            //ContentValues values = new ContentValues();
            /*values.put(D_ID,dids[i]);*/
            values.put(D_NAME,dnames[i]);
//            db.insert(DEPARTMENT,null,values);
        }
        db.insert(DEPARTMENT,null,values);
        db.close();

    }


    public List<String> getAllDepartments(){
        List<String> departments = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM Department";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                departments.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return departments;
    }


    public int getDepIDbyName(String dname){


        SQLiteDatabase db = this.getReadableDatabase();
        int did = 1;
        String name;
        String query = "SELECT name, d_id FROM " + DEPARTMENT;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do{
                name = cursor.getString(cursor.getColumnIndex("name"));

                if(name.equals(dname) ){
                    did = cursor.getInt(cursor.getColumnIndex("d_id"));
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();

        return did;

    }




    public Boolean UserNameNotExist(String uname){


        SQLiteDatabase db = this.getReadableDatabase();
        String a = "null";

        String query = "SELECT name FROM Users ";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(cursor.getColumnIndex("name"));
                if(a.equals(uname)){
                    return false;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return true;



    }






    public Boolean ProjectNameNotExist(String pname){


        SQLiteDatabase db = this.getReadableDatabase();
        String a = "null";

        String query = "SELECT name FROM Project ";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do{
                a = cursor.getString(cursor.getColumnIndex("name"));
                if(a.equals(pname)){
                    return false;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return true;



    }



}





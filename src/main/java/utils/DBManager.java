package utils;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

//import com.mysql.jdbc.Driver;
import models.Course;

/**
 * Created by jdriver on 10/29/18.
 * this will handle base db utilities and any requests not applicable to an object
 */
public class DBManager {

    private String dbPath = "jdbc:sqlite:test.db";
    private Connection conn;

    public DBManager() {
        try{
            // this either accesses or creates the db
            conn = DriverManager.getConnection(dbPath);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * this method needs to build the initial db the first time we start the application.  Thereafter it should detect
     * that it has already been build and skip.
     */
    public void buildDB() {
        //TODO build the whole thing

    }

    // needs to be called whenever we are done with the db.
    public void closeDB() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(new Course("ID221","Sample Class 1", "Fall 2018"));
        courses.add(new Course("ID221","Sample Class 2", "Fall 2018"));
        courses.add(new Course("ID221","Sample Class 3", "Fall 2018"));
        courses.add(new Course("ID221","Sample Class 4", "Fall 2018"));
        return courses;
    }
}

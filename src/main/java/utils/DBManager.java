package utils;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;

//import com.mysql.jdbc.Driver;
import models.Course;

/**
 * Created by jdriver on 10/29/18.
 * this will handle base db utilities and any requests not applicable to an object
 */
public class DBManager {

    private String dbPath = "jdbc:sqlite:gradium.db";
    private Connection conn;

    public DBManager() {
        try{
            Class.forName("org.sqlite.JDBC");
            // this either accesses or creates the db
            conn = DriverManager.getConnection(dbPath);
//            Course ex = new Course("2", "example", "2012", "Fall");
            //this.addCourse(ex);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    /**
     * this method needs to build the initial db the first time we start the application.  Thereafter it should detect
     * that it has already been build and skip.
     */
    public void buildDB() {
        System.out.println("Building the tables");
        final String studentQuery = "CREATE TABLE `student` ( `BU_ID` VARCHAR(200) NOT NULL , `first_name` VARCHAR(200) NOT NULL , `middle_intial` VARCHAR(1) NOT NULL , `family_name` VARCHAR(200) NOT NULL , `type` VARCHAR(20) NOT NULL , `email` VARCHAR(200) NOT NULL , PRIMARY KEY (`BU_ID`))";
        final String classQuery = "CREATE TABLE `class` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `class` VARCHAR(400) NOT NULL , `semester` VARCHAR(400) NOT NULL , `name` VARCHAR(400) NOT NULL , `year` VARCHAR(400) NOT NULL )";
        final String class_assignments = "CREATE TABLE `class_assignments` ( `BU_ID` VARCHAR(200) NOT NULL , `Class_ID` INT(200) NOT NULL , PRIMARY KEY (`BU_ID`))";
        final String course_assignments = "CREATE TABLE `course_assignments` ( `BU_ID` VARCHAR(200) NOT NULL , `assignment_ID` INT(200) NOT NULL , PRIMARY KEY (`BU_ID`))";
        final String assignments = "CREATE TABLE `assignments` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `class_ID` INT(200) NOT NULL , `name` INT(200) NOT NULL, `description` VARCHAR(700) NULL , `score` INT(11) NOT NULL , `extra_credit` INT(11) NOT NULL, `type` VARCHAR(200) NULL )";
        final String group = "CREATE TABLE `groups` ( `group_id` INT(200) NOT NULL , `BU_ID` VARCHAR(200) NOT NULL , `class_id` INT(200) NOT NULL , PRIMARY KEY (`BU_ID`))";
        final String weight = "CREATE TABLE `weight` ( `group_id` INT(200) NOT NULL , `assignment_ID` INT(200) NOT NULL , `weight` INT(11) NOT NULL , PRIMARY KEY (`group_id`))";

        try {
            Statement stmt = this.conn.createStatement();
                stmt.execute(studentQuery);
                stmt.execute(classQuery);
                stmt.execute(class_assignments);
                stmt.execute(course_assignments);
                stmt.execute(assignments);
                stmt.execute(group);
                stmt.execute(weight);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void dropAllTables() {
        System.out.println("Building the tables");
        final String studentQuery = "DROP TABLE `student`";
        final String classQuery = "DROP TABLE `class`";
        final String class_assignments = "DROP TABLE `class_assignments`";
        final String course_assignments = "DROP TABLE `course_assignments`";
        final String assignments = "DROP TABLE `assignments`";
        final String group = "DROP TABLE `groups`";
        final String weight = "DROP TABLE `weight`";

        try {
            Statement stmt = this.conn.createStatement();
            stmt.execute(studentQuery);
            stmt.execute(classQuery);
            stmt.execute(class_assignments);
            stmt.execute(course_assignments);
            stmt.execute(assignments);
            stmt.execute(group);
            stmt.execute(weight);

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // needs to be called whenever we are done with the db.
    public void closeDB() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(Course course){
        String courseName = course.getName();
        String sectionNum = course.getSectionNumber();
        String year = course.getYear();
        String semester = course.getSemester();
        String insertQuery = "INSERT INTO `class` (class, semester, name, year) VALUES(?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, sectionNum);
                pstmt.setString(2, semester);
                pstmt.setString(3, courseName);
                pstmt.setString(4, year);
                pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        String selectQuery = "SELECT ID, class, semester, name, year  FROM `class`";

        try {
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                courses.add(new Course(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        courses.add(new Course("ID221","Sample Class 1", "Fall 2018"));
//        courses.add(new Course("ID221","Sample Class 2", "Fall 2018"));
//        courses.add(new Course("ID221","Sample Class 3", "Fall 2018"));
//        courses.add(new Course("ID221","Sample Class 4", "Fall 2018"));
        return courses;
    }
}

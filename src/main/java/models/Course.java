package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Group;
import utils.DBManager;

public class Course {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;
    private String semester;
    private DBManager db = new DBManager();

    public Course(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.sectionNumber = rs.getString("class");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
            this.semester = rs.getString("semester");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(String sectionNumber, String name, String year, String semester) {
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
        this.semester = semester;
        //DBManager db = new DBManager();
    }

    public String getName() {
        return name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getYear(){ return year; }

    public String getSemester(){ return semester;}

    public Group getGroup(Student student) {
        //TODO: to be pulled from the db


        return new Group(1, "Sample Group", this);
    }

    public void addAssignment(Assignment assignment){
        String insertQuery = "INSERT INTO `assignments` (class_ID, name, description, score, extra_credit, type) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setInt(1, this.id);
            pstmt.setString(2, assignment.getName());
            pstmt.setString(3, assignment.getDescription());
            pstmt.setInt(4, assignment.getValue());
            pstmt.setInt(5, assignment.getExtraCredit());
            pstmt.setString(6, assignment.getType());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Assignment> getAssignments() {

        //TODO: db call to retrieve and build an assignment list
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        String selectQuery = "SELECT ID, class_ID, name FROM `class` WHERE class_id = '" + this.id + "'";

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                assignments.add(new Assignment(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 1"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 2"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 3"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 4"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 5"));

        return assignments;
    }

    public ArrayList<Student> getStudents() {
        //CREATE TABLE `student` ( `BU_ID` VARCHAR(200) NOT NULL , `first_name` VARCHAR(200) NOT NULL , `middle_intial` VARCHAR(1) NOT NULL , `family_name` VARCHAR(200) NOT NULL , `type` VARCHAR(20) NOT NULL , `email` VARCHAR(200) NOT NULL , PRIMARY KEY (`BU_ID`))"
        //"CREATE TABLE `class_assignments` ( `BU_ID` VARCHAR(200) NOT NULL , `Class_ID` INT(200) NOT NULL , PRIMARY KEY (`BU_ID`))";
        //TODO: db call to retrieve and build a student list
        ArrayList<Student> students = new ArrayList<Student>();
        String selectQuery = "SELECT first_name, middle_intial, family_name, type, email FROM `student` AS A" +
                "INNER JOIN `class_assignments` AS B ON B.BU_ID = A.BU_ID" +
                " WHERE A.class_id = '" + this.id + "'";

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                students.add(new Student(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//
//        students.add(new Student("ID101", "Joe", "m", " Driver", "Graduate", "Sample1"));
//        students.add(new Student("ID102", "Armin", "n", " Sabouri", "Undergrad", "Sample2"));
//        students.add(new Student("ID103", "Katie", "", " Quirk", "Graduate", "Sample3"));
        //students.add(new Student("ID104", "Some Guy", "PHD", "Sample4"));

        return students;
    }
}

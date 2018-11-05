package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Group;
import utils.DBManager;

public class Course {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;
    private String semester;

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

    public ArrayList<Assignment> getAssignments() {

        //TODO: db call to retrieve and build an assignment list
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 1"));
        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 2"));
        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 3"));
        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 4"));
        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 5"));

        return assignments;
    }

    public ArrayList<Student> getStudents() {

        //TODO: db call to retrieve and build a student list
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(new Student("ID101", "Joe", "m", " Driver", "Graduate", "Sample1"));
        students.add(new Student("ID102", "Armin", "n", " Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID103", "Katie", "", " Quirk", "Graduate", "Sample3"));
        //students.add(new Student("ID104", "Some Guy", "PHD", "Sample4"));

        return students;
    }
}

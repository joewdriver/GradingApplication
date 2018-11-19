package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import models.Group;
import utils.DBManager;

public class Course {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;
    private String season;

    public Course(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.sectionNumber = rs.getString("section number");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
            this.season = rs.getString("season");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(String sectionNumber, String name, String year, String season) {
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getYear() { return year; }

    public String getSeason() { return season; }

    public Group getGroup(Student student) {
        //TODO: to be pulled from the db
        return new Group(1, "Sample Group", this);
    }

    public ArrayList<Assignment> getAssignments() {

        //TODO: db call to retrieve and build an assignment list
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        assignments.add(new Assignment(this.getSectionNumber(),"homework 1", "homework", 100));
        assignments.add(new Assignment(this.getSectionNumber(),"quiz 1", "quiz", 100));
        assignments.add(new Assignment(this.getSectionNumber(),"homework 2", "homework", 100));
        assignments.add(new Assignment(this.getSectionNumber(),"quiz 2", "quiz", 100));
        assignments.add(new Assignment(this.getSectionNumber(),"homework 3", "homework", 100));

        Collections.sort(assignments);
        return assignments;
    }

    public ArrayList<Student> getStudents() {

        //TODO: db call to retrieve and build a student list
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(new Student("ID101", "Joe Driver", "Graduate", "Sample1"));
        students.add(new Student("ID102", "Armin Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID103", "Katie Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID104", "Some Guy", "PHD", "Sample4"));
        students.add(new Student("ID105", "Joe Driver", "Graduate", "Sample1"));
        students.add(new Student("ID106", "Armin Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID107", "Katie Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID108", "Some Guy", "PHD", "Sample4"));
        students.add(new Student("ID109", "Joe Driver", "Graduate", "Sample1"));
        students.add(new Student("ID110", "Armin Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID111", "Katie Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID112", "Some Guy", "PHD", "Sample4"));

        return students;
    }
}

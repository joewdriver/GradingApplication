package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Course {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;

    public Course(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.sectionNumber = rs.getString("section number");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(String sectionNumber, String name, String year) {
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getGroup(String buId) {
        //TODO: to be pulled from the db
        return "random group";
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
        students.add(new Student("ID101", "Joe Driver", "Graduate", "Sample1"));
        students.add(new Student("ID102", "Armin Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID103", "Katie Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID104", "Some Guy", "PHD", "Sample4"));

        return students;
    }
}

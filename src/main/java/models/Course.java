package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import models.Group;
import utils.DBManager;
import utils.Strings;

public class Course {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;
    private String season;

    public Course(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.sectionNumber = rs.getString("class");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
            this.season = rs.getString("semester");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(int id, String sectionNumber, String name, String year, String season) {
        this.id = id;
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
        this.season = season;
    }

    public int getId() { return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getYear() { return year; }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() { return season; }

    public void setSeason(String season) {
        this.season = season;
    }

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
        students.add(new Student("ID101", "Joe", "a", "Driver", "Graduate", "Sample1"));
        students.add(new Student("ID102", "Armin", "a", "Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID103", "Katie", "a", "Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID104", "Some", "a", "Guy", "PHD", "Sample4"));
        students.add(new Student("ID105", "Joe", "a", "Driver", "Graduate", "Sample1"));
        students.add(new Student("ID106", "Armin", "a", "Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID107", "Katie", "a", "Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID108", "Some", "a", "Guy", "PHD", "Sample4"));
        students.add(new Student("ID109", "Joe", "a", "Driver", "Graduate", "Sample1"));
        students.add(new Student("ID110", "Armin", "a", "Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID111", "Katie", "a", "Quirk", "Graduate", "Sample3"));
        students.add(new Student("ID112", "Some", "a", "Guy", "PHD", "Sample4"));

        return students;
    }

    /**
     * either insert a new course into the db, or updates the existing course depending on whether a valid id
     * is found.  If inserting, it will also update its own id field with the newly created one
     */
    public void save() {
        String query;
        DBManager db = new DBManager();
        // first, if this is a new course we run an insert
        if(this.id == -1) {
            query = String.format(Strings.createCourse,this.sectionNumber, this.season, this.name, this.year);
            db.executeUpdate(query);
        // this will cover updates of existing objects
        } else {
            query = String.format(Strings.updateCourse,this.sectionNumber, this.season, this.name, this.year, this.id);
            db.executeUpdate(query);
        }
        db.closeDB();
    }
}

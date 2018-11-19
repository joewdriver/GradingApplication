package models;

import utils.ContextButton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
    private int id;
    private Course course;
    private String name;

    public Group(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            //TODO: we'll need some extra activity to get a course object in here
//            this.course = rs.getInt("course");
            this.name = rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Group(int id, String name, Course course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public String getName() {
        return this.name;
    }

    public Course getCourse() {
        return this.course;
    }

    public ArrayList<Student> getStudents() {
        //TODO: replace this with a db call
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(new Student("ID101","Joe", "a", "Driver","Graduate", "sample"));
        students.add(new Student("ID102","Katie", "a", "Quirk","Graduate", "sample"));
        students.add(new Student("ID103","Armin", "a", "Sabouri","Undergraduate", "sample"));
        students.add(new Student("ID104","Some", "a", "Guy","PHD", "sample"));

        return students;
    }
}

package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Student {
    private String buId;
    private String name;
    private String graduateLevel;
    private String email;

    public Student(ResultSet rs) {
        try {
            this.buId = rs.getString("buId");
            this.name = rs.getString("name");
            this.graduateLevel = rs.getString("graduateLevel");
            this.email = rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student(String buId, String name, String graduateLevel, String email) {
        this.buId = buId;
        this.name = name;
        this.graduateLevel = graduateLevel;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getGraduateLevel() {
        return this.graduateLevel;
    }

    public String getBuId() {
        return this.buId;
    }

    public String getEmail() { return this.email; }

    public int getGrade(String classId) {
        //TODO: DB call here
        return 100;
    }

    //TODO: turn this into an actual query instead of mocked data
    public ArrayList<Course> getClasses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(new Course(-1, "ID101", "Fake Course", "2022","Fall"));
        courses.add(new Course(-1, "ID102", "Fake Course", "2023","Spring"));
        courses.add(new Course(-1, "ID103", "Fake Course", "2024", "Summer"));
        return courses;
    }
}

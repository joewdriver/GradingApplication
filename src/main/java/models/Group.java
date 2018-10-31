package models;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public String getName() {
        return this.name;
    }

    public Course getCourse() {
        return this.course;
    }
}

package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment implements Comparable<Assignment>{
    private int id;
    private int totalPoints;
    private String classId;
    private String name;
    private String type;
    private String description;

    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("classId");
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.type = rs.getString("type");
            this.totalPoints = rs.getInt("totalPoints");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     */
    public Assignment(String classId, String name, String type, int totalPoints) {
        this.classId = classId;
        this.name = name;
        this.type = type;
        this.totalPoints = totalPoints;
    }

    /**
     * a constructor that handles a default type setting to homework
     */
    public Assignment(String classId, String name) {
        this.classId = classId;
        this.name = name;
        this.type = "Homework";
        this.totalPoints = 100;
        this.description = "This is a sample description";
    }

    public String getName() {
        return this.name;
    }

    public int compareTo(Assignment assignment) {
        return type.compareTo(assignment.getType());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public int getScore(Student student) {
        //TODO add db call here to get score for a given student, or return null if student has no score
        return 99;
    }

    /**
     * retrieves the course name and ID in which the assignment exists
     */
    public Course getCourse() {
        //TODO: this needs to be replaced with a db call
        return new Course("ID201","Sample Course","2018","Spring");
    }

    public double getAverageScore() {
        //TODO: this needs to be replaced with a db call
        return 95.5;
    }
}

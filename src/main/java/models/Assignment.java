package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment implements Comparable<Assignment>{
    private int id;
    private String classId;
    private String name;
    private String assignmentType;
    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("classId");
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.assignmentType = rs.getString("assignmentType");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     */
    public Assignment(String classId, String name, String assignmentType) {
        this.classId = classId;
        this.name = name;
        this.assignmentType = assignmentType;
    }

    public String getName() {
        return this.name;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public int compareTo(Assignment assignment) {
        return assignmentType.compareTo(assignment.getAssignmentType());
    }
}

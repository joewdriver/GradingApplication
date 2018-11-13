package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment {
    private int id;
    private String classId;
    private String name;
    private String description;
    private int value;
    private int extraCredit;
    private String type;
    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("class_ID");
            this.id = rs.getInt("ID");
            this.name = rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getExtraCredit() {
        return extraCredit;
    }

    public int getId() {
        return id;
    }

    public String getClassId() {
        return classId;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     */
    public Assignment(String classId, String name) {
        this.classId = classId;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

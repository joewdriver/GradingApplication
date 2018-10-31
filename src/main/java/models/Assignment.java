package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment {
    private int id;
    private String classId;
    private String name;
    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("classId");
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

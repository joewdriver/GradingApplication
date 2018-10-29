package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Assignment {
    private int id;
    private String classId;
    private String name;
    private int score;
    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("classId");
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.score = rs.getInt("score");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     * @param classId
     * @param name
     * @param score
     * @return
     */
//    public Assignment createAssignment(String classId, String name, int score) {
//
//    }
}

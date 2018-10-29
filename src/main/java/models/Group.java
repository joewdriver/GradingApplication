package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
    private int id;
    private int classId;

    public Group(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            int classId = rs.getInt("classId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

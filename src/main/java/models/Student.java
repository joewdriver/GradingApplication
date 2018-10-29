package models;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}

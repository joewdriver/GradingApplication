package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Class {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;

    public Class(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.sectionNumber = rs.getString("section number");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}

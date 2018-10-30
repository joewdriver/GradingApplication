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

    public Class(String sectionNumber, String name, String year) {
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getGroup(String buId) {
        //TODO: to be pulled from the db
        return "random group";
    }
}

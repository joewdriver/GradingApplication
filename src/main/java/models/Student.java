package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import utils.DBManager;

public class Student {
    private String buId;
    private String first_name;
    private String middle_name;
    private String family_name;

    private String graduateLevel;
    private String email;
    private DBManager db = new DBManager();
    public Student(ResultSet rs) {
        try {
            this.buId = rs.getString("buId");
            this.first_name = rs.getString("first_name");
            this.middle_name = rs.getString("middle_intial");
            this.family_name = rs.getString("family_name");
            this.graduateLevel = rs.getString("type");
            this.email = rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student(String buId, String first_name, String middle_name, String family_name, String graduateLevel, String email) {
        this.buId = buId;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.family_name = family_name;
        this.graduateLevel = graduateLevel;
        this.email = email;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public String getMiddle_name() {
        return this.middle_name;
    }

    public String getFamily_name() {
        return this.family_name;
    }

    public String getGraduateLevel() {
        return this.graduateLevel;
    }

    public String getBuId() {
        return this.buId;
    }

    public String getEmail() { return this.email; }

    public int getGrade(String classId) {
        //TODO: DB call here
        return 100;
    }



    //TODO: turn this into an actual query instead of mocked data
    public ArrayList<Course> getClasses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        String selectQuery = "SELECT ID, class, semester, name, year  FROM `class` as A " +
                "INNER JOIN `class_assignments` as B on B.Class_ID = A.ID" +
                "WHERE B.BU_ID = " + this.buId;
        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                courses.add(new Course(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return courses;
    }
}

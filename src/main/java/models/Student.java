package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            this.graduateLevel = rs.getString("graduateLevel");
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

    public int getGrade(String classId) {
        //TODO: DB call here
        return 100;
    }

    public void addClass(int ClassId){
        String insertQuery = "INSERT INTO `class_assignments` (BU_ID, Class_ID) VALUES(?,?)";
        try {
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setString(1, this.buId);
            pstmt.setInt(2, ClassId);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO: turn this into an actual query instead of mocked data
    public ArrayList<Course> getClasses() {
        ArrayList<Course> courses = new ArrayList<Course>();

        courses.add(new Course("ID101", "Fake Course", " 2022", "Fall"));
        courses.add(new Course("ID102", "Fake Course", " 2023", "Fall"));
        courses.add(new Course("ID103", "Fake Course", " 2024", "Fall"));
        return courses;
    }
}

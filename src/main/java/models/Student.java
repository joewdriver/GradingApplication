package models;

import utils.DBManager;
import utils.Strings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Student {
    private String buId;
    private String firstName;
    private String middleInitial;
    private String familyName;
    private String graduateLevel;
    private String email;

    public Student(ResultSet rs) {
        try {
            this.buId = rs.getString("buId");
            this.firstName = rs.getString("first_name");
            this.middleInitial = rs.getString("middle_initial");
            this.familyName = rs.getString("family_name");
            this.graduateLevel = rs.getString("graduateLevel");
            this.email = rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student(String buId, String first_name, String middle_initial, String family_name,
                   String graduateLevel, String email) {
        this.buId = buId;
        this.firstName = first_name;
        this.middleInitial = middle_initial;
        this.familyName =  family_name;
        this.graduateLevel = graduateLevel;
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getFullName() {
        return firstName + " " + middleInitial + " " + familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getGraduateLevel() {
        return this.graduateLevel;
    }

    public void setGraduateLevel(String graduateLevel) {
        this.graduateLevel = graduateLevel;
    }

    public String getBuId() {
        return this.buId;
    }

    public void setBuId(String buId) {
        this.buId = buId;
    }

    public String getEmail() { return this.email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrade(String classId) {
        //TODO: DB call here
        return 100;
    }

    //TODO: turn this into an actual query instead of mocked data
    public ArrayList<Course> getClasses() {
        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(new Course(-1, "ID101", "Fake Course", "2022","Fall"));
        courses.add(new Course(-1, "ID102", "Fake Course", "2023","Spring"));
        courses.add(new Course(-1, "ID103", "Fake Course", "2024", "Summer"));
        return courses;
    }

    /**
     * either creates a new student or updates an existing one based on whether or not an existing ID is passed in.
     */
    public void save() {
        // first check and see if there are any results for the student ID
        boolean insert = true;
        ResultSet rs;
        DBManager db = new DBManager();
        try {
            // if a query for this id returns null, then we flip to insert instead of update
            rs = db.executeQuery(String.format(Strings.getStudentFromId, buId));
            if(rs.next()) {
                insert = false;
            }

            // if we run this as an insert
            if(insert) {
                db.executeUpdate(String.format(Strings.insertStudent,buId,firstName,middleInitial,
                        familyName,graduateLevel,email));
            // if we run as an update
            } else {
                db.executeUpdate(String.format(Strings.updateStudent, firstName, middleInitial, familyName,
                        graduateLevel, email, buId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.closeDB();
    }
}

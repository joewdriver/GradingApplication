package models;


import java.sql.PreparedStatement;

import utils.DBManager;
import utils.Strings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import utils.DBManager;

public class Student {
    private String buId;

    private String firstName;
    private String middleInitial;
    private String familyName;
    private String graduateLevel;
    private String email;
    private DBManager db = new DBManager();
    public Student(ResultSet rs) {
        try {
            this.buId = rs.getString("BU_ID");
            this.firstName = rs.getString("first_name");
            this.middleInitial = rs.getString("middle_initial");
            this.familyName = rs.getString("family_name");
            this.graduateLevel = rs.getString("type");
            this.email = rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student(String buId, String first_name, String middle_name, String family_name, String graduateLevel, String email) {
        this.buId = buId;
        this.firstName = first_name;
        this.middleInitial = middle_name;
        this.familyName = family_name;

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

    public double getGrade(String classId) {
        /*
        * @details: provide the average grade
        * */
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Integer> totals = new ArrayList<Integer>();
        double rsum = 0.0;

        String selectQuery = "SELECT ID, class_ID, name, type, totalPoints, score  FROM `assignments` as A " +
                "INNER JOIN `course_assignments` as B on B.Class_ID = A.ID" +
                "WHERE B.BU_ID = " + this.buId + " AND A.class_ID = '" + classId + "'";
        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                scores.add(rs.getInt("score"));
                totals.add(rs.getInt("totalPoints"));
                assignments.add(new Assignment(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        int scoreIdx = 0;
        for (Assignment assign : assignments){
            int assignIdx = 0;
            selectQuery = "SELECT weight FROM `weights` WHERE  assignment_ID = '" + assign.getId() + "'";
            try {
                Statement stmt  = this.db.getConn().createStatement();
                ResultSet rs    = stmt.executeQuery(selectQuery);
                while (rs.next() ) {
                    if(totals.get(scoreIdx) != 0)
                        rsum += rs.getInt("weight") * (scores.get(scoreIdx) / totals.get(scoreIdx));
                    else
                        rsum += 0 ;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            scoreIdx++;
        }
        return rsum;
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

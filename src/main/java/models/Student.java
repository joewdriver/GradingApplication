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
    private String type;

    private String notes;

    public Student(ResultSet rs) {
        try {
            this.buId = rs.getString("BU_ID");
            this.firstName = rs.getString("first_name");
            this.middleInitial = rs.getString("middle_initial");
            this.familyName = rs.getString("family_name");
            this.graduateLevel = rs.getString("type");
            this.email = rs.getString("email");
            this.type = rs.getString("type");
            this.notes = rs.getString("notes");
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
        this.notes = "";
    }

    public Student(String buId, String first_name, String middle_name, String family_name, String graduateLevel,
                   String email, String notes) {
        this.buId = buId;
        this.firstName = first_name;
        this.middleInitial = middle_name;
        this.familyName = family_name;
        this.graduateLevel = graduateLevel;
        this.email = email;
        this.notes = notes;
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

    public String getType(){return type;}

    public String getFullName() {
        return firstName + " " + middleInitial + " " + familyName;
    }

    public String getNotes() {
        return notes;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getScore(int assignmentId){
        DBManager db = new DBManager();
        double score = 0.0;
        String selectQuery = "SELECT score FROM `course_assignments` WHERE BU_ID = '"+this.buId+"' AND assignment_ID = "+assignmentId+" ";
        try {
            Statement stmt  = db.getConn().createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);
            while(rs.next()){
                score = rs.getDouble("score");
            }
            rs.close();
            stmt.close();
        }catch (Exception e) {
            e.printStackTrace();
            db.closeDB();
        }

        db.closeDB();
        return score;
    }

    public double getGrade(int classId) {

        /*
        * @details: provide the average grade
        * */
        DBManager db = new DBManager();
        System.out.println("in get score");
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Integer> totals = new ArrayList<Integer>();
        double rsum = 0.0;

        String selectQuery = "SELECT A.totalPoints, B.score  FROM `assignments` as A " +
                "INNER JOIN `course_assignments` as B on B.assignment_ID = A.ID " +
                "WHERE B.BU_ID = '" + this.buId + "' AND A.class_ID = '" + classId + "'";

        try {

            ResultSet rs = db.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                scores.add(rs.getInt("score"));
                totals.add(rs.getInt("totalPoints"));
                assignments.add(new Assignment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            db.closeDB();
        }
        System.out.println("should close db here");
        db.closeDB();
        db = new DBManager();

        int scoreIdx = 0;
        for (Assignment assign : assignments){
            int assignIdx = 0;
            // TODO: move to strings
            selectQuery = "SELECT weight FROM `weight` WHERE  assignment_ID = '" + assign.getId() + "'";
            try {

                ResultSet rs = db.executeQuery(selectQuery);
                while (rs.next() ) {
                    if(totals.get(scoreIdx) != 0)
                        rsum += rs.getInt("weight") * (scores.get(scoreIdx) / totals.get(scoreIdx));
                    else
                        rsum += 0 ;
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                db.closeDB();
            }
            scoreIdx++;
        }
        return rsum;
    }

    public void setScore(Assignment assignment, Student student, float score){
        DBManager db = new DBManager();
        String selectQuery = "UPDATE course_assignments SET score = '"+score+"' WHERE assignment_ID = '" + assignment.getId() +"' AND BU_ID = '"+student.getBuId()+"'";
        System.out.println(selectQuery);
        try {
            Statement stmt  = db.getConn().createStatement();
            stmt.executeUpdate(selectQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeDB();
        }
        db.closeDB();
    }

    public ArrayList<Course> getClasses() {
        DBManager db = new DBManager();
        ArrayList<Course> courses = new ArrayList<Course>();
        // TODO: move query to strings
        String selectQuery = "SELECT * FROM class as A " +
                "INNER JOIN class_assignments AS B on B.class_ID = A.ID " +
                "WHERE B.BU_ID = '" + this.buId + "'";

        try {
            Statement stmt  = db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                courses.add(new Course(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        db.closeDB();
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
                        familyName,graduateLevel,email,notes));
            // if we run as an update
            } else {
                db.executeUpdate(String.format(Strings.updateStudent, firstName, middleInitial, familyName,
                        graduateLevel, email, notes, buId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.closeDB();
    }
}

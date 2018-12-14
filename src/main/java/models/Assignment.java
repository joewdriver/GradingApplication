package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DBManager;
import utils.Strings;

public class Assignment implements Comparable<Assignment>{
    private int id;
    private int totalPoints;
    private int classId;
    private String name;
    private String description;
    private int value;
    private int extraCredit;
    private String type;



    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getInt("class_ID");
            this.id = rs.getInt("ID");
            this.name = rs.getString("name");
            this.type = rs.getString("type");
            this.totalPoints = rs.getInt("totalPoints");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     */
    public Assignment(int classId, String name, String type, int totalPoints) {
        this.classId = classId;
        this.name = name;
        this.type = type;
        this.totalPoints = totalPoints;
    }


    public int getExtraCredit() {
        return extraCredit;
    }

    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getValue() {
        return value;
    }

    public static int getLastId(){
        String assignmentQuery = Strings.getLastCreatedAssignment;
        int assignmentId = 0;

        DBManager tempdb = new DBManager();
        ResultSet rs = tempdb.executeQuery(assignmentQuery);
        try {
            rs.next();
            assignmentId = rs.getInt("seq");
        } catch (SQLException e) {
            e.printStackTrace();
            tempdb.closeDB();
        }
        tempdb.closeDB();

        return assignmentId;
    }

    public float getWeight(boolean newAssignment){
        DBManager db = new DBManager();
        if(newAssignment) {
            return  1;
        }
        float weight = 0;
        String selectQuery = "SELECT weight FROM `weights` WHERE assignment_ID = "+this.getId()+" ";
        try {
            ResultSet rs = db.executeQuery(selectQuery);
            weight += rs.getInt("weight");
        }catch (Exception e) {
            e.printStackTrace();
            db.closeDB();
        }

        db.closeDB();
        return weight;

    }

    /**
     * a constructor that handles a default type setting to homework
     */
    public Assignment(int classId, String name) {
        this.classId = classId;
        this.name = name;
        this.type = "Homework";
        this.totalPoints = 100;
        this.description = "This is a sample description";
    }

    public String getName() {
        return this.name;
    }

    public int compareTo(Assignment assignment) {
        return type.compareTo(assignment.getType());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public double getScore(Student student) {
        //TODO replace this with a call to the student assignment join table ARMIN IS DOING THIS DONT TOUCH
        return 100.0;
    }

    /**
     * retrieves the course name and ID in which the assignment exists
     */
    public Course getCourse() {
        DBManager db = new DBManager();
    /*ID");
            this.sectionNumber = rs.getString("class");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
            this.season = rs.getString("semester");
            this.active = rs.getInt("active");*/
        String selectQuery = "SELECT A.class, A.name, A.year, A.semester, A.ID, A.active  FROM class AS A " +
                "INNER JOIN assignments AS B ON A.ID = B.class_id " +
                "WHERE B.class_id = '" + this.classId + "'";

        try {
            ResultSet rs = db.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                Course course = new Course(rs);
                db.closeDB();
                return course; //should only be one course
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeDB();
        }
        db.closeDB();
        return null;

    }

    /**
     * averages the scores for this assignment across all students
     * @return the average score
     */
    public double getAverageScore() {
        String query = String.format(Strings.getAssignmentScores, this.id);
        DBManager db = new DBManager();
        int score = 0;
        int count = 0;

        try {
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                count++;
                score = score + rs.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            db.closeDB();
        }
        db.closeDB();
        count = (count == 0) ? 1:count;
        int result = score/count;
        return result;
    }

    public void save() {
        DBManager db = new DBManager();
        String query = String.format(Strings.updateAssignment,this.totalPoints,
                this.name, this.description, this.type, this.id);
        db.executeUpdate(query);
        db.closeDB();
        //TODO find a way to save weighting, or remove weighting from assignment edit view
    }

    public void saveNew(int courseId) {
        DBManager db = new DBManager();
        String query = String.format(Strings.addAssignmentToCourse, courseId, this.name, this.description,
                this.extraCredit, this.type, this.totalPoints);
        db.executeUpdate(query);
        db.closeDB();

        this.id = Assignment.getLastId();

    }
}

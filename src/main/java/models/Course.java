package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import models.Group;
import org.sqlite.core.DB;
import utils.DBManager;
import utils.Strings;

public class Course implements Comparable<Course> {
    private int id;
    private String sectionNumber;
    private String name;
    private String year;
    private String season;
    private int active;

    private String semester;
//    private DBManager db = new DBManager();

    public Course(ResultSet rs) {
        try {
            this.id = rs.getInt("ID");
            this.sectionNumber = rs.getString("class");
            this.name = rs.getString("name");
            this.year = rs.getString("year");
            this.season = rs.getString("semester");
            this.active = rs.getInt("active");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Course(int id, String sectionNumber, String name, String year, String season) {
        this.id = id;
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
        this.season = season;
    }

    public Course(int id) {
        this.id = id;
    }
    public Course cloneCourse() {
        // first add a new course identical to this one
        String courseInsert = String.format(Strings.createCourse, getSectionNumber(), getSeason(), getName(), getYear(), 1);
        DBManager tempdb = new DBManager();
        tempdb.executeUpdate(courseInsert);
        tempdb.closeDB();
        // next retrieve the new course id
        String courseQuery = Strings.getLastCreatedCourse;
        int courseId = 0;

        tempdb = new DBManager();
        ResultSet rs = tempdb.executeQuery(courseQuery);
        try {
            rs.next();
            courseId = rs.getInt("seq");
        } catch (SQLException e) {
            e.printStackTrace();
            tempdb.closeDB();
        }
        tempdb.closeDB();

        // once the new course has been created, represent it as a data object
        Course newCourse = new Course(courseId);

        // finally add each assignment to the new course
        for(Assignment assignment:getAssignments()) {
            System.out.println("Checkpoint Alpha");
            newCourse.addAssignment(assignment, 0, assignment.getId());
        }

        // return the newly created course
        return newCourse;
    }

    public void deleteClass(){
        DBManager db = new DBManager();
        db.deleteCourse(this);
        db.closeDB();
    }

    public int compareTo(Course course) {

        boolean larger = (Integer.parseInt(this.year) > Integer.parseInt(course.getYear()));
        if(larger) {
            return 1;
        } else if(!larger) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getId() { return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public boolean  getActive() {
        return this.active == 1;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getYear(){ return year; }

    public String getSemester(){ return season;}
  
    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeason() { return season; }

    public void setSeason(String season) {
        this.season = season;
    }

    public Group getGroup(Student student) {
        ArrayList<Assignment> groups = new ArrayList<Assignment>();
        String id = student.getBuId();
        String selectQuery = "SELECT * FROM `groups` WHERE  BU_ID = '" + id + "'";
        DBManager db = new DBManager();
        try {

            Statement stmt  = db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                groups.add(new Assignment(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeDB();
        //not sure what name is going to look like
        return new Group(1, "Sample Group", this);
    }

    public void addAssignment(Assignment assignment, float weight, int curr_assignmentID){
        DBManager tempdb = new DBManager();
        String insertQuery = Strings.addAssignmentToCourse;
        try {

            PreparedStatement pstmt = tempdb.getConn().prepareStatement(insertQuery);
            pstmt.setInt(1, this.id);
            pstmt.setString(2, assignment.getName());
            pstmt.setString(3, assignment.getDescription());
            pstmt.setInt(4, assignment.getValue());
            pstmt.setInt(5, assignment.getExtraCredit());
            pstmt.setString(6, assignment.getType());
            pstmt.setInt(7,assignment.getTotalPoints());
            pstmt.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        tempdb.closeDB();
        DBManager db = new DBManager();
        for(Student tempStudent : this.getStudents()){
            insertQuery = "INSERT INTO course_assignments ( BU_ID, assignment_ID, score)  VALUES(?,?,?)";
            try {
                System.out.println("THE  ID: "  + curr_assignmentID);
                PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
                pstmt.setString(1, tempStudent.getBuId());
                pstmt.setInt(2, curr_assignmentID);
                pstmt.setFloat(3,0);
                pstmt.executeUpdate();

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        db.closeDB();

        db = new DBManager();
            insertQuery = "INSERT INTO weights ( group_id, assignment_ID, weight)  VALUES(?,?,?)";
            try {
                PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
                pstmt.setInt(1, -1);
                pstmt.setInt(2, curr_assignmentID);
                pstmt.setFloat(3,weight);
                pstmt.executeUpdate();

            }catch (SQLException e) {
                e.printStackTrace();
            }
        db.closeDB();

        ArrayList<Student> students = this.getStudents();
        for(Student student: students){
            db = new DBManager();
            insertQuery = "INSERT INTO course_assignments ( BU_ID, assignment_ID, score)  VALUES(?,?,?)";
            try {
                PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
                pstmt.setString(1,  student.getBuId());
                pstmt.setInt(2, curr_assignmentID);
                pstmt.setFloat(3,weight);
                pstmt.executeUpdate();

            }catch (SQLException e) {
                e.printStackTrace();
            }
            db.closeDB();
        }

    }

    public void deleteAssignment(Assignment assignment){
        DBManager db = new DBManager();
        int id = assignment.getId();
        String deleteQuery = "DELETE FROM `assignments` WHERE class_ID = ?";
        try {

             PreparedStatement pstmt = db.getConn().prepareStatement(deleteQuery);

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeDB();

    }

    public ArrayList<Assignment> getAssignments() {
        DBManager db = new DBManager();
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        String selectQuery = "SELECT class_ID, ID, name, type, totalPoints FROM `assignments` WHERE class_ID = '" + this.id + "'";

        try {

            Statement stmt  = db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                assignments.add(new Assignment(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        db.closeDB();

        Collections.sort(assignments);
        return assignments;
    }

    public ArrayList<Student> getStudents() {
        DBManager db = new DBManager();
        ArrayList<Student> students = new ArrayList<Student>();
        String selectQuery = String.format(Strings.getStudentsInClass, this.id);

        try {
            ResultSet rs = db.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                students.add(new Student(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            db.closeDB();
        }
        return students;
    }

    public void deleteStudent(Student student){
        String id = student.getBuId();
        String deleteQuery = "DELETE FROM `class_assignments` WHERE class_ID = ? AND BU_ID = ?";
        try {
            DBManager db = new DBManager();
            PreparedStatement pstmt = db.getConn().prepareStatement(deleteQuery);

            // set the corresponding param
            pstmt.setInt(1, this.id);
            pstmt.setString(2, id);
            // execute the delete statement
            pstmt.executeUpdate();
            db.closeDB();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addStudent(Student student){
        String insertQuery = "INSERT INTO `class_assignments` (BU_ID, Class_ID) VALUES(?,?)";
        try {
            DBManager db = new DBManager();
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setString(1, student.getBuId());
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
            db.closeDB();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        insertQuery = "INSERT INTO student ( BU_ID, first_name, middle_initial, family_name, type, email, notes) "+
                "VALUES(?,?,?,?,?,?,?)";
        try {
            DBManager db = new DBManager();
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setString(1, student.getBuId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getMiddleInitial());
            pstmt.setString(4, student.getFamilyName());
            pstmt.setString(5, student.getGraduateLevel());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getNotes());
            pstmt.executeUpdate();
            db.closeDB();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * either insert a new course into the db, or updates the existing course depending on whether a valid id
     * is found.  If inserting, it will also update its own id field with the newly created one
     */
    public void save() {
        String query;
        DBManager db = new DBManager();
        // first, if this is a new course we run an insert
        if(this.id == -1) {
            query = String.format(Strings.createCourse,this.sectionNumber, this.season, this.name, this.year, 1);
            System.out.println(query);
            db.executeUpdate(query);
        // this will cover updates of existing objects
        } else {
            query = String.format(Strings.updateCourse,this.sectionNumber, this.season, this.name, this.year, this.active, this.id);
            db.executeUpdate(query);
        }
        db.closeDB();
    }

    public double getMeanScore() {
        ArrayList<Student> students = getStudents();
        int count = 0;
        double total = 0.0;
        for(Student student:students) {
//            total += student.getGrade(sectionNumber);
            count++;
        }
        double mean = total/count;
        //return total;
        return 0.0;
    }

    public double getMedianScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
//            scores.add(student.getGrade(sectionNumber));
        }
        Collections.sort(scores);
        int middle = scores.size()/2;
//        return scores.get(2);
        return 0.0;
    }

    public double getHighScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
//            scores.add(student.getGrade(sectionNumber));
        }
        Collections.sort(scores);
//        return scores.get(0);
        return 0.0;
    }

    public double getLowScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
//            scores.add(student.getGrade(sectionNumber));
        }
        Collections.sort(scores);
        Collections.reverse(scores);
       // return scores.get(0);
        return 0.0;

    }
}

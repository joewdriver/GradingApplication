package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
    private DecimalFormat df = new DecimalFormat();

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
        this.active = 1;
    }

    public Course(int id, String sectionNumber, String name, String year, String season, int active) {
        this.id = id;
        this.sectionNumber = sectionNumber;
        this.name = name;
        this.year = year;
        this.season = season;
        this.active = active;
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
            newCourse.addCloneAssignment(assignment);
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
        return new Group(1, "Sample Group", this);
    }

    public void addAssignment(Assignment assignment){
        DBManager db = new DBManager();
        for(Student tempStudent : this.getStudents()){
            String insertQuery = "INSERT INTO course_assignments ( BU_ID, assignment_ID, score)  VALUES(?,?,?)";
            try {
                PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
                pstmt.setString(1, tempStudent.getBuId());
                pstmt.setInt(2, assignment.getId());
                pstmt.setFloat(3,0);
                pstmt.executeUpdate();

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        db.closeDB();
    }

    public void addCloneAssignment(Assignment assignment){
        DBManager db = new DBManager();
        String insertQuery = String.format(Strings.addAssignmentToCourse, this.id, assignment.getName(),
                assignment.getDescription(),assignment.getExtraCredit(),assignment.getType(),
                assignment.getTotalPoints(),assignment.getUgradWeight(),assignment.getGradWeight(),
                assignment.getUgradWeightType(), assignment.getGradWeightType());
        db.executeUpdate(insertQuery);
        db.closeDB();
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
        String selectQuery = "SELECT class_ID, ID, name, type, totalPoints, ugrad_weight, grad_weight, " +
                "ugrad_weight_type, grad_weight_type FROM `assignments` WHERE class_ID = '" + this.id + "'";

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
        DBManager db = new DBManager();
        try {
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setString(1, student.getBuId());
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
            db.closeDB();
        }catch (SQLException e) {
            db.closeDB();
            e.printStackTrace();
        }

        for(Assignment assignment : this.getAssignments()){
            System.out.println("CHECKPOINT A");
            insertQuery = "INSERT INTO `course_assignments` (BU_ID, assignment_ID, score) VALUES(?,?, ?)";
            db = new DBManager();
            try {

                PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
                pstmt.setString(1, student.getBuId());
                pstmt.setInt(2, assignment.getId());
                pstmt.setInt(3, 0);
                pstmt.executeUpdate();
                db.closeDB();
            }catch (SQLException e) {
                e.printStackTrace();
                db.closeDB();
            }
        }


        insertQuery = "INSERT INTO student ( BU_ID, first_name, middle_initial, family_name, type, email, notes) "+
                "VALUES(?,?,?,?,?,?,?)";
        db = new DBManager();
        try {

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
        db.closeDB();
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
            query = String.format(Strings.updateCourse,this.sectionNumber, this.season, this.name, this.year,
                    this.active, this.id);
            db.executeUpdate(query);
        }
        db.closeDB();
    }

    public String getMeanScore() {
        ArrayList<Student> students = getStudents();
        int count = 0;
        float total = 0;
        for(Student student:students) {
            total += student.getGrade(this.id);
            count++;
        }
        float mean = total/count;
        this.df.setMaximumFractionDigits(2);

        return df.format(mean);
    }

    public String getMedianScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
            scores.add(student.getGrade(this.id));
        }
        Collections.sort(scores);
        int middle = scores.size()/2;
        if (scores.isEmpty())
            return "0";
        this.df.setMaximumFractionDigits(2);
        return df.format(scores.get(middle));
    }

    public String getHighScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
            scores.add(student.getGrade(this.id));
        }
        Collections.sort(scores);
        if (scores.isEmpty())
            return "0";
        this.df.setMaximumFractionDigits(2);
        return this.df.format(scores.get(0));
    }

    public String getLowScore() {
        ArrayList<Student> students = getStudents();
        ArrayList<Double> scores = new ArrayList<Double>();
        for(Student student:students) {
           scores.add(student.getGrade(this.id));
        }
        Collections.sort(scores);
        Collections.reverse(scores);
        if (scores.isEmpty())
            return "0";
        return df.format(scores.get(0));

    }
}

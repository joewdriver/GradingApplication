package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import models.Group;
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
    private DBManager db = new DBManager();

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
        //TODO resolve db work for clone a course.  Should include assignments, but not students.
        return this;
    }

    public void deleteClass(){
        this.db.deleteCourse(this);
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
        return active == 1;
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

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                groups.add(new Assignment(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        //not sure what name is going to look like
        return new Group(1, "Sample Group", this);
    }

    public void addAssignment(Assignment assignment){
        String insertQuery = "INSERT INTO `assignments` (class_ID, name, description, score, extra_credit, type, totalPoints) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
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

    }

    public void deleteAssignment(Assignment assignment){
        int id = assignment.getId();
        String deleteQuery = "DELETE FROM `assignments` WHERE class_ID = ?";
        try {
             PreparedStatement pstmt = this.db.getConn().prepareStatement(deleteQuery);

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<Assignment> getAssignments() {
        //TODO: db call to retrieve and build an assignment list
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        String selectQuery = "SELECT class_ID, ID, name, type FROM `assignments` WHERE class_ID = '" + this.id + "'";

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                assignments.add(new Assignment(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 1"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 2"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 3"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 4"));
//        assignments.add(new Assignment(this.getSectionNumber(),"Assignment 5"));


        Collections.sort(assignments);
        return assignments;
    }

    // TODO: move query into strings.  Query is causing SQLite exception.
    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        String selectQuery = "SELECT A.BU_ID, A.first_name, A.middle_initial, A.family_name, A.type, A.email, " +
                "A.notes FROM student AS A INNER JOIN class_assignments AS B ON B.BU_ID = A.BU_ID " +
                " WHERE B.class_ID = '" + this.id + "'";
        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                System.out.println("got one");
                students.add(new Student(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        students.add(new Student("ID101", "Joe", "m", " Driver", "Graduate", "Sample1"));
//        students.add(new Student("ID102", "Armin", "n", " Sabouri", "Undergrad", "Sample2"));
//        students.add(new Student("ID103", "Katie", "", " Quirk", "Graduate", "Sample3"));


        return students;
    }

    public void deleteStudent(Student student){
        String id = student.getBuId();
        String deleteQuery = "DELETE FROM `class_assignments` WHERE class_ID = ? AND BU_ID = ?";
        try {
            PreparedStatement pstmt = this.db.getConn().prepareStatement(deleteQuery);

            // set the corresponding param
            pstmt.setInt(1, this.id);
            pstmt.setString(2, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addStudent(Student student){
        String insertQuery = "INSERT INTO `class_assignments` (BU_ID, Class_ID) VALUES(?,?)";
        try {
            PreparedStatement pstmt = db.getConn().prepareStatement(insertQuery);
            pstmt.setString(1, student.getBuId());
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        insertQuery = "INSERT INTO student ( BU_ID, first_name, middle_initial, family_name, type, email, notes) "+
                "VALUES(?,?,?,?,?,?,?)";
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
        // first, if this is a new course we run an insert
        if(this.id == -1) {
            query = String.format(Strings.createCourse,this.sectionNumber, this.season, this.name, this.year, 1);
            System.out.println(query);
            this.db.executeUpdate(query);
        // this will cover updates of existing objects
        } else {
            query = String.format(Strings.updateCourse,this.sectionNumber, this.season, this.name, this.year, this.active, this.id);
            this.db.executeUpdate(query);
        }
        this.db.closeDB();
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

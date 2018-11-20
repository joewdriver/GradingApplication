package models;

import utils.ContextButton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import utils.DBManager;

public class Group {
    private int id;
    private Course course;
    private String name;
    private DBManager db = new DBManager();

    public Group(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            //TODO: we'll need some extra activity to get a course object in here
//            this.course = rs.getInt("course");
            this.name = rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Group(int id, String name, Course course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public String getName() {
        return this.name;
    }

    public Course getCourse() {
        return this.course;
    }

    public int getId() { return id; }

    public void deleteStudent(Student student){
        String id = student.getBuId();
        String deleteQuery = "DELETE FROM `groups` WHERE BU_ID = ?";
        try {
            PreparedStatement pstmt = this.db.getConn().prepareStatement(deleteQuery);
            // set the corresponding param
            pstmt.setString(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Student> getStudents() {
        //TODO: replace this with a db call
        ArrayList<Student> students = new ArrayList<Student>();
        String selectQuery = "SELECT first_name, middle_intial, family_name, type, email FROM `student` AS A" +
                "INNER JOIN `groups` AS B ON B.BU_ID = A.BU_ID" +
                " WHERE B.group_id = '" + this.id + "'";

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                students.add(new Student(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        students.add(new Student("ID101","Joe","m", " Driver","Graduate", "sample"));
        students.add(new Student("ID102","Katie", "m", " Quirk","Graduate", "sample"));
        students.add(new Student("ID103","Armin", "m", " Sabouri","Undergraduate", "sample"));
        students.add(new Student("ID104","Some Guy","PHD", "sample"));


        return students;
    }
}

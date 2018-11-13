package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.DBManager;

public class Assignment {
    private int id;
    private String classId;
    private String name;
    private String description;
    private int value;
    private int extraCredit;
    private String type;
    private int totalPoints;
    private DBManager db = new DBManager();


    /**
     * constructor to build a code object based on a result set from MySQL.
     * @param rs the result set passed in
     */
    public Assignment(ResultSet rs) {
        try {
            this.classId = rs.getString("class_ID");
            this.id = rs.getInt("ID");
            this.name = rs.getString("name");
            this.type = rs.getString("type");
            this.totalPoints = rs.getInt("totalPoints");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getExtraCredit() {
        return extraCredit;
    }

    public int getId() {
        return id;
    }

    public String getClassId() {
        return classId;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * to be used when creating a new assignment.  Should insert the new assignment in to the db, then return
     * the created assignment object.  can call the other constructor
     */
    public Assignment(String classId, String name, String type, int totalPoints) {
        this.classId = classId;
        this.name = name;
        this.type = type;
        this.totalPoints = totalPoints;
    }

    public Assignment(String classId, String name) {
        this.classId = classId;
        this.name = name;
        this.type = "Homework";
        this.totalPoints = 100;
        this.description = "This is a sample description";
    }

    public void setDescription(String description) {
        //TODO: update the db
        this.description = description;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public Course getCourse() {
        //TODO: this needs to be replaced with a db call
        //TABLE `class` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT , `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
        // //`class` VARCHAR(400) NOT NULL , `semester` VARCHAR(400) NOT NULL , `name` VARCHAR(400) NOT NULL , `year` VARCHAR(400) NOT NULL )";

        String selectQuery = "ID, class, semester, name, year  FROM `class` AS A " +
                "INNER JOIN `assignments` AS B ON A.class_ID = B.class_ID " +
                "WHERE B.class_ID = '" + this.classId + "'";

        try {
            Statement stmt  = this.db.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(selectQuery);
            // loop through the result set
            while (rs.next()) {
                return new Course(rs); //should only be one course
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public double getAverageScore() {
        //TODO: this needs to be replaced with a db call
        return 95.5;
    }


    public String getName() {
        return this.name;
    }
}

package utils;

/**
 * Created by jdriver on 11/19/18.
 */
public class Strings {
    public static final String getAllCourses = "SELECT * FROM class";

    public static final String createCourse = "INSERT INTO class (\n" +
            " class, semester, name, year, active)\n" +
            "VALUES\n" +
            " (\"%s\",\"%s\",\"%s\",\"%s\",%d);";


    public static final String updateCourse = "UPDATE class SET " +
            "class=\"%s\", semester=\"%s\", name=\"%s\", year=\"%s\", active=%d " +
            "WHERE ID=\"%s\";";

    public static final String getStudentFromId = "SELECT * FROM student where BU_ID=\"%s\"";

    public static final String insertStudent = "INSERT INTO student (" +
            "BU_ID, first_name, middle_initial, family_name, type, email) " +
            "VALUES" +
            "(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")";

    public static final String updateStudent = "UPDATE student SET " +
            "first_name=\"%s\", middle_initial=\"%s\", family_name=\"%s\", type=\"%s\", email=\"%s\" " +
            "WHERE BU_ID = \"%s\"";

    public static final String getLastCreatedCourse = "select seq from sqlite_sequence where name=\"class\"\n";

    public static final String addAssignmentToCourse = "INSERT INTO `assignments` (class_ID, name, description, score, extra_credit, type, totalPoints) VALUES(?,?,?,?,?,?,?)";

}

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
            "BU_ID, first_name, middle_initial, family_name, type, email, notes) " +
            "VALUES" +
            "(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")";

    public static final String updateStudent = "UPDATE student SET " +
            "first_name=\"%s\", middle_initial=\"%s\", family_name=\"%s\", type=\"%s\", email=\"%s\", "+
            "notes=\"%s\" WHERE BU_ID = \"%s\"";

    public static final String getLastCreatedCourse = "select seq from sqlite_sequence where name=\"class\"\n";

    public static final String getLastCreatedAssignment = "select seq from sqlite_sequence where name=\"assignments\"\n";

    public static final String addAssignmentToCourse = "INSERT INTO `assignments` (class_ID, name, description, " +
            "extra_credit, type, totalPoints, ugrad_weight, grad_weight, ugrad_weight_type, grad_weight_type) " +
            "VALUES(%d,\"%s\",\"%s\",%d,\"%s\",%d,%f,%f,%f,%f)";


    public static final String getAssignmentScores = "SELECT score from course_assignments where assignment_ID=%d";

    public static final String getStudentsInClass = "SELECT A.BU_ID, A.first_name, A.middle_initial, A.family_name, A.type, A.email, " +
            "A.notes FROM student AS A INNER JOIN class_assignments AS B ON B.BU_ID = A.BU_ID " +
            " WHERE B.class_ID = %d";

    public static final String updateAssignment = "UPDATE assignments set totalPoints='%s', name='%s', " +
            "description='%s', type='%s', ugrad_weight=%f, grad_weight=%f, ugrad_weight_type=%f, grad_weight_type=%f" +
            " where ID=%d";

    public static final String updateWeight = "UPDATE weight set weight =%s where group_id=%s and assignment_id=%s";

}

package utils;

/**
 * Created by jdriver on 11/19/18.
 */
public class Strings {
    public static final String getAllCourses = "SELECT * FROM class";
    public static final String createCourse = "INSERT INTO class (\n" +
            " class, semester, name, year)\n" +
            "VALUES\n" +
            " (\"%s\",\"%s\",\"%s\",\"%s\");";
    public static final String updateCourse = "UPDATE class SET " +
            "class=\"%s\", semester=\"%s\", name=\"%s\", year=\"%s\"" +
            "WHERE ID=\"%s\";";
}

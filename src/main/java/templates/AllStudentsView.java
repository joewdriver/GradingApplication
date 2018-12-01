package templates;

import models.Assignment;
import models.Course;
import models.Group;
import models.Student;
import utils.ContextButton;
import utils.DBManager;
import utils.View;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class AllStudentsView extends View {
    private DBManager db = new DBManager();
    private ArrayList<Student> students = new ArrayList<Student>();
    private JButton viewAllCoursesButton;
    private ActionListener alViewAllCourses;
    private ActionListener alStudentView;

    public AllStudentsView() {
        setup(1200, 800,"All Students");
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {
        ArrayList<Student> students = this.db.getAllStudents();

        // TODO: remove this and make it actually pull from the db
        students.add(new Student("ID101", "Joe", "m", " Driver", "Graduate", "Sample1"));
        students.add(new Student("ID102", "Armin", "n", " Sabouri", "Undergrad", "Sample2"));
        students.add(new Student("ID103", "Katie", "", " Quirk", "Graduate", "Sample3"));

        alViewAllCourses = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllCourses();
            }
        };

        // action listener for the row headers
        alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToStudent((Student)btn.getContext());
            }
        };

        viewAllCoursesButton = new JButton("View all Courses");
        viewAllCoursesButton.addActionListener(alViewAllCourses);

    }

    private void buildLayout() {
        // this is the overall parent
        Container pane = getContentPane();

        // our panels
        JPanel headerPanel =  new JPanel();
        JPanel studentPanel = new JPanel();

        // our layouts
        GroupLayout headerLayout = new GroupLayout(headerPanel);
        GroupLayout studentLayout = new GroupLayout(studentPanel);

        // creates gaps between edge of window and components
        headerLayout.setAutoCreateContainerGaps(true);
        studentLayout.setAutoCreateContainerGaps(true);

        // these two groups will represent the header
        GroupLayout.Group headerHorizontal = headerLayout.createSequentialGroup();
        GroupLayout.Group headerVertical = headerLayout.createParallelGroup(CENTER);

        // the top level label and the button should appear in a single row
        headerHorizontal
                .addComponent(viewAllCoursesButton);

        headerVertical
                .addComponent(viewAllCoursesButton);

        // these two groups will handle the list of class buttons
        GroupLayout.Group listHorizontal = studentLayout.createParallelGroup(CENTER);
        GroupLayout.Group listVertical = studentLayout.createSequentialGroup();

        // populate the groups with our ui elements in order, with the header panel at the top
        listHorizontal.addComponent(headerPanel);
        listVertical.addComponent(headerPanel);
        //listHorizontal.addComponent(studentPanel);
        //listVertical.addComponent(studentPanel);

        for(Student student : students) {
            // if we are using a search term, verify the course qualifies before adding it in
            if(true) {      //TODO: add in sort functionality
                //System.out.println("Checkpoint 2 " + searchTerm);
                ContextButton btn = new ContextButton(
                        student.getFullName(), student);
                btn.addActionListener(this.alStudentView);
                listHorizontal.addComponent(btn);
                listVertical.addComponent(btn);
            }
        }

        /*
        headerPanel.add(viewAllCoursesButton);
        //JPanel studentPanel = new JPanel(new GridLayout(students.size() + 1,1));
        String col[] = {"BU ID","First Name","Middle Initial", "Family Name", "Graduate Level", "Email"};

        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        JTable studentTable = new JTable(tableModel);

        GroupLayout coreLayout = new GroupLayout(framePanel);
        coreLayout.setAutoCreateContainerGaps(true);

        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(headerPanel)
                .addComponent(studentTable)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(studentTable)
        );
        framePanel.setLayout(coreLayout);

        // Populate our student table
        for(Student student : students) {
            String buId = student.getBuId();
            String firstName = student.getFirstName();
            String middleInitial = student.getMiddleInitial();
            String familyName = student.getFamilyName();
            String graduateLevel = student.getGraduateLevel();
            String email = student.getEmail();

            Object[] data = {buId, firstName, middleInitial, familyName, graduateLevel, email};

            tableModel.addRow(data);

            //studentPanel.add(new JLabel(student.getFullName()));
        }
        */

        //pane.setLayout(new GridBagLayout());
        //pane.add(studentPanel);

        // now we apply the groups to the layout
        studentLayout.setHorizontalGroup(listHorizontal);
        studentLayout.setVerticalGroup(listVertical);

        studentPanel.setLayout(studentLayout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(studentPanel);

    }

    private void goToStudent(Student student) {
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        dispose();
    }

    private void viewAllCourses() {
        CoursesView coursesView = new CoursesView();
        coursesView.setVisible(true);
        dispose();
    }

}

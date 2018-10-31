package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.ContextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CourseView extends JFrame {
    private ContextButton addStudentButton;
    private ContextButton importStudentsButton;
    private ContextButton editClassSettings;
    private JButton saveButton;
    private JLabel classNameHeader;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students = new ArrayList<Student>();
    private Course course;
    private ActionListener alStudentView;
    private ActionListener alAssignmentView;
    private ActionListener alSave;
    private ActionListener alSettings;
    private ActionListener alAddStudent;
    private ActionListener alImportStudents;



    public CourseView(Course course) {
        this.course = course;
        this.createUIComponents();
        setTitle("Grading Records - " + course.getName());

        // going to need alot more space with this one
        setSize(1200,800);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        assignments = course.getAssignments();
        students = course.getStudents();

        classNameHeader = new JLabel("Editing grades for " + course.getName());

        saveButton = new JButton("Save");
        alSave = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                save();
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

        // action listener for the column headers
        alAssignmentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToAssignment((Assignment)btn.getContext());
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

        alSettings = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                goToSettings((Course)btn.getContext());
            }
        };

        alAddStudent = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                addStudent((Course)btn.getContext());
            }
        };

        alImportStudents = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                importStudents((Course)btn.getContext());
            }
        };

        addStudentButton = new ContextButton("Add A Student", this.course);
        editClassSettings = new ContextButton("Class Setting", this.course);
        importStudentsButton = new ContextButton("Import Student List", this.course);

        addStudentButton.addActionListener(alAddStudent);
        editClassSettings.addActionListener(alSettings);
        importStudentsButton.addActionListener(alImportStudents);
    }

    /**
     * our most complicated view.  To get this going we're going to have a GridLayout and two GroupLayouts
     * embedded into a third GroupLayout, all within containing panels
     */
    private void buildLayout() {
        // this is the overall parent
        Container pane = getContentPane();

        JPanel framePanel = new JPanel();
        JPanel headerPanel =  new JPanel();
        JPanel footerPanel =  new JPanel();
        JPanel centralPanel = new JPanel(new GridLayout(students.size() + 1,assignments.size() + 2));

        // core overall layout vertical
        GroupLayout coreLayout = new GroupLayout(framePanel);
        coreLayout.setAutoCreateContainerGaps(true);

        // core layout grouping to order our member panels high to low
        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(headerPanel)
                .addComponent(centralPanel)
                .addComponent(footerPanel)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(centralPanel)
                .addComponent(footerPanel)
        );

        framePanel.setLayout(coreLayout);

        // horizontal header layout.  Currently has a single item, but built to be flexible for additions
        GroupLayout headerLayout = new GroupLayout(headerPanel);

        headerLayout.setVerticalGroup(headerLayout.createParallelGroup(CENTER)
                .addComponent(classNameHeader));

        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(classNameHeader));


        // grid layout organizes itself into one long array, a total pain, so we have to add everything sequentially
        // first the empty top left corner
        centralPanel.add("top left", new JLabel(""));
        centralPanel.add("top left", new JLabel("Weighted Average"));

        // next the assignment list in the top row
        for(Assignment assignment:assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignmentView);
            centralPanel.add(btn);
        }

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(Student student: students) {
            ContextButton btn = new ContextButton(student.getName(), student);
            btn.addActionListener(this.alStudentView);
            centralPanel.add(btn);
            //TODO: add the average calculation here based on db call
            centralPanel.add(new TextField("100"));
            for(Assignment assignment:assignments) {
                // TODO: resolve this based on db call of student assignment join
                centralPanel.add(new TextField("100"));
            }
        }

        // footer layout for various functional buttons
        GroupLayout footerLayout = new GroupLayout(footerPanel);

        footerLayout.setHorizontalGroup(footerLayout.createParallelGroup(CENTER)
            .addComponent(editClassSettings)
            .addComponent(addStudentButton)
            .addComponent(importStudentsButton));

        footerLayout.setHorizontalGroup(footerLayout.createSequentialGroup()
            .addComponent(editClassSettings)
            .addComponent(addStudentButton)
            .addComponent(importStudentsButton)
            .addComponent(saveButton));


        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(framePanel);
    }

    private void goToSettings(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void addStudent(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void importStudents(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void save() {
        //TODO: save function needs to read the scores, update the db, then reload the app
        CourseView courseView = new CourseView(this.course);
        courseView.setVisible(true);
        dispose();
    }

    private void goToStudent(Student student) {
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        dispose();
    }

    private void goToAssignment(Assignment assignment) {
        //TODO: add assignment view transition here
    }
}

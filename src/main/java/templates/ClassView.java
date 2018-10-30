package templates;

import models.Student;

import javax.swing.*;
import javax.swing.GroupLayout.Group;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class ClassView extends JFrame {
    private JButton addStudentButton;
    private JButton importStudentsButton;
    private JButton editClassSettings;
    private JButton saveButton;
    private JLabel classNameHeader;
    private ArrayList<JButton> assignments = new ArrayList<JButton>();
    private ArrayList<JButton> students = new ArrayList<JButton>();
    private String className;


    public ClassView(String className) {
        this.className = className;
        this.createUIComponents();
        setTitle("Grading Records - " + className);

        // going to need alot more space with this one
        setSize(1200,800);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        // TODO: remove this and populate these arraylists with a db call.
        assignments.add(new JButton("Assignment 1"));
        assignments.add(new JButton("Assignment 2"));
        assignments.add(new JButton("Assignment 3"));
        assignments.add(new JButton("Assignment 4"));
        assignments.add(new JButton("Assignment 5"));

        students.add(new JButton("Joe"));
        students.add(new JButton("Katie"));
        students.add(new JButton("Armin"));
        students.add(new JButton("Some Guy"));

        // action listener for the column headers
        ActionListener alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //TODO: create a non-dummy student from context
                goToStudent(new Student("Sample ID", "Sample Name", "UnderGrad", "fake-email"));
            }
        };
        for(JButton student:students) {
            student.addActionListener(alStudentView);
        }

        classNameHeader = new JLabel("Editing grades for " + className);

        editClassSettings = new JButton("Class Settings");
        ActionListener alSettings = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                goToSettings();
            }
        };
        editClassSettings.addActionListener(alSettings);

        addStudentButton = new JButton("Add a Student");
        ActionListener alAdd = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                addStudent();
            }
        };
        addStudentButton.addActionListener(alAdd);

        importStudentsButton = new JButton("Import Student List");
        ActionListener alImport = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                importStudents();
            }
        };
        importStudentsButton.addActionListener(alImport);

        saveButton = new JButton("Save");
        ActionListener alSave = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                save();
            }
        };
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
        for(JButton assignment:assignments) {
            centralPanel.add(assignment.getName(), assignment);
        }

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(JComponent student: students) {
            centralPanel.add(student.getName(),student);
            //TODO: add the average calculation here based on db call
            centralPanel.add(new TextField("100"));
            for(JComponent assignment:assignments) {
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

    private void goToSettings() {
        //TODO: install view transition here
        System.exit(0);
    }

    private void addStudent() {
        //TODO: install view transition here
        System.exit(0);
    }

    private void importStudents() {
        //TODO: install view transition here
        System.exit(0);
    }

    private void save() {
        //TODO: call save function, reload page to recalculate scores
        System.exit(0);
    }

    private void goToStudent(Student student) {
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        dispose();
    }
}

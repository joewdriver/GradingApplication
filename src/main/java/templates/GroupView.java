package templates;

import models.Assignment;
import models.Course;
import models.Group;
import models.Student;
import utils.ContextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class GroupView extends JFrame {
    private JButton addStudentButton;
    private JButton importStudentsButton;
    private JButton editClassSettings;
    private JButton saveButton;
    private JLabel groupNameHeader;
    private ArrayList<Assignment> assignments;
    private ArrayList<JButton> students = new ArrayList<JButton>();
    private Group group;
    private ActionListener alAssignments;


    public GroupView(Group group) {
        this.group = group;
        this.createUIComponents();
        setTitle("Grading Records - " + group.getCourse().getName() + " " + group.getName());

        // going to need alot more space with this one
        setSize(1200,800);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        assignments = group.getCourse().getAssignments();

        // TODO: remove this and populate this arraylist with a call to Group.getStudents.
        students.add(new ContextButton("Joe", new Student("ID101","Joe Driver","Graduate", "sample")));
        students.add(new ContextButton("Katie", new Student("ID102","Katie Quirk","Graduate", "sample")));
        students.add(new ContextButton("Armin", new Student("ID103","Armin Sabouri","Undergraduate", "sample")));
        students.add(new ContextButton("Some Guy", new Student("ID104","Some Guy","PHD", "sample")));

        // action listener for the column headers
        ActionListener alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToStudent((Student)btn.getContext());
            }
        };
        for(JButton student:students) {
            student.addActionListener(alStudentView);
        }

        groupNameHeader = new JLabel("Editing grades for " + group.getCourse().getName() + " " + group.getName());

        saveButton = new JButton("Save");
        ActionListener alSave = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                save();
            }
        };

         alAssignments = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton)e.getSource();
                goToAssignment((Assignment)btn.getContext());
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
                .addComponent(groupNameHeader));

        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(groupNameHeader));


        // grid layout organizes itself into one long array, a total pain, so we have to add everything sequentially
        // first the empty top left corner
        centralPanel.add("top left", new JLabel(""));
        centralPanel.add("top left", new JLabel("Weighted Average"));

        // next the assignment list in the top row
        for(Assignment assignment:assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignments);
            centralPanel.add(btn, assignment);
        }

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(JComponent student: students) {
            centralPanel.add(student.getName(),student);
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

    private void save() {
        //TODO: save function needs to read the scores, update the db, then reload the app
        GroupView courseView = new GroupView(this.group);
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

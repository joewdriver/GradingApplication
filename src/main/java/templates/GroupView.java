package templates;

import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
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
    private JButton saveButton;
    private JLabel groupNameHeader;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students;
    private Group group;
    private ActionListener alAssignments;
    private ActionListener alStudentView;


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
        students = group.getStudents();

        groupNameHeader = new JLabel("Add Students/Update Weights");
        saveButton = new JButton("Update");

        // action listener for the column headers
        ActionListener alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToStudent((Student)btn.getContext());
            }
        };

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
     * we're going to have 2 tables of 2 columns in addition to header and footer management
     */
    private void buildLayout() {
        // this is the overall parent
        Container pane = getContentPane();

        JPanel framePanel = new JPanel();
        JPanel headerPanel = new JPanel();
        JPanel footerPanel = new JPanel();
        JPanel weightPanel = new JPanel(new GridLayout(2, assignments.size() + 1));
        JPanel spacerPanel = new JPanel();
        JPanel gradesPanel = new JPanel(new GridLayout(students.size() + 1, assignments.size()+2));

        GroupLayout coreLayout = new GroupLayout(framePanel);
        coreLayout.setAutoCreateContainerGaps(true);

        // Build the parent container first
        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(spacerPanel)
                .addComponent(weightPanel)
                .addComponent(spacerPanel)
                .addComponent(gradesPanel)
                .addComponent(footerPanel));

        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup(CENTER)
                .addComponent(headerPanel)
                .addComponent(spacerPanel)
                .addComponent(weightPanel)
                .addComponent(spacerPanel)
                .addComponent(gradesPanel)
                .addComponent(footerPanel));

        framePanel.setLayout(coreLayout);

        // next the header
        GroupLayout headerLayout = new GroupLayout(headerPanel);
        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(groupNameHeader));
        headerLayout.setVerticalGroup(headerLayout.createParallelGroup()
                .addComponent(groupNameHeader));
        headerPanel.setLayout(headerLayout);

        // now the assignments and weights.  first add assignment label
        weightPanel.add(new JLabel("Assignment"));
        // loop through the assignment list and set them as headers
        for(Assignment assignment: assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignments);
            weightPanel.add(btn);
        }
        // add the weight row label
        weightPanel.add(new Label("Weight"));
        // loop again to add weight value
        for(Assignment assignment: assignments) {
            weightPanel.add(new JTextField("1"));
        }

        spacerPanel.add(Box.createVerticalStrut(10));


        // next we need to build the student/grade list. start with student and weight average headers
        gradesPanel.add(new JLabel("Student"));
        gradesPanel.add(new JLabel("Weighted Average"));

        // Assignments will be headers
        for(Assignment assignment:assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignments);
            gradesPanel.add(btn);
        }
        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(Student student: students) {
            ContextButton btn = new ContextButton(student.getName(), student);
            btn.addActionListener(this.alStudentView);
            gradesPanel.add(btn);
            //TODO: add the weighted calculation here based on db call
            gradesPanel.add(new JLabel("     100"));
            for(Assignment assignment:assignments) {
                // TODO: resolve this based on db call of student assignment join
                gradesPanel.add(new JLabel("     100"));
            }
        }


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

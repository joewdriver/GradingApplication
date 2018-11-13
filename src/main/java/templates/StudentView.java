package templates;

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

public class StudentView extends JFrame {
    private ArrayList<Course> courses;
    private JLabel name;
    private JLabel graduateLevel;
    private JComboBox groups;
    private Student student;
    private ContextButton editStudent;

    public StudentView(Student student) {
        this.student = student;
        this.courses = student.getClasses();
        this.createUIComponents();
        setTitle("Grading Records - " + student.getFamily_name());
        setSize(1200,800);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        name = new JLabel(student.getFamily_name());
        graduateLevel = new JLabel(student.getGraduateLevel());

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                goToEditStudent((Student)btn.getContext());
            }
        };
        editStudent = new ContextButton("Edit Student", this.student);
        editStudent.addActionListener(al);

    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel corePanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new GridLayout(courses.size() + 1,3));

        // top level vertical container for two sub-panels
        GroupLayout coreLayout = new GroupLayout(corePanel);

        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(topPanel)
                .addComponent(bottomPanel)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(topPanel)
                .addComponent(bottomPanel)
        );

        corePanel.setLayout(coreLayout);

        GroupLayout topLayout = new GroupLayout(topPanel);
        topLayout.setHorizontalGroup(topLayout.createSequentialGroup()
                .addComponent(name)
                .addComponent(graduateLevel)
                .addComponent(editStudent)
        );

        topLayout.setVerticalGroup(topLayout.createParallelGroup(CENTER)
                .addComponent(name)
                .addComponent(graduateLevel)
                .addComponent(editStudent)
        );

        topPanel.setLayout(topLayout);

        coreLayout.setAutoCreateContainerGaps(true);
        topLayout.setAutoCreateContainerGaps(true);

        // three column headers, fixed in this grid, we never get wider
        bottomPanel.add("top left", new JLabel("Course"));
        bottomPanel.add("top left", new JLabel("Grade"));
        bottomPanel.add("top left", new JLabel("Group"));

        // set up an even listener for courses, and another for groups
        ActionListener alGoToClass = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                ContextButton btn = (ContextButton)e.getSource();
                goToCourse((Course)btn.getContext());
            }
        };

        ActionListener alGoToGroup = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToGroup((Group)btn.getContext());
            }
        };

        // now loop the courses -- avoid the reserved term
        for(Course section: courses) {
            // add the action listener to the button
            ContextButton tempClass = new ContextButton(section.getSectionNumber() + " " + section.getName(), section);
            tempClass.addActionListener(alGoToClass);
            bottomPanel.add(tempClass);

            // display the grade as a non-editable label
            bottomPanel.add(new Label(Integer.toString(student.getGrade(section.getSectionNumber())),JLabel.CENTER));

            // add an action listener to the group
            ContextButton tempGroup = new ContextButton(section.getGroup(student).getName(),section.getGroup(student));
            tempGroup.addActionListener(alGoToGroup);
            bottomPanel.add(tempGroup);
        }

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(corePanel);
    }

    private void goToCourse(Course course) {
        CourseView classes = new CourseView(course);
        classes.setVisible(true);
        dispose();
    }

    private void goToGroup(Group group) {
        GroupView groupView = new GroupView(group);
        groupView.setVisible(true);
        dispose();
    }

    private void goToEditStudent(Student student) {
        EditStudentView editView = new EditStudentView(student);
        editView.setVisible(true);
        dispose();
    }
}

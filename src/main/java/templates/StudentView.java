package templates;

import models.Course;
import models.Group;
import models.Student;
import utils.ContextButton;
import utils.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class StudentView extends View {
    private static final int NOTE_ROWS = 4;
    private static final int NOTE_COLUMNS = 50;

    private ArrayList<Course> courses;
    private JLabel name;
    private JLabel graduateLevel, email, buID;
    private JTextArea notes;
    private JComboBox groups;
    private Student student;
    private ContextButton editStudent;
    private JButton allStudentsButton, saveButton;
    private ActionListener alAllStudents, alSaveNotes;

    public StudentView(Student student) {
        this.student = student;
        this.courses = student.getClasses();

        setup(1200, 800, "Gradium " + student.getFullName());
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {

        name = new JLabel(student.getFullName()+" \t");
        graduateLevel = new JLabel("Graduate Level: "+student.getGraduateLevel());
        email = new JLabel("Email: "+student.getEmail());
        buID = new JLabel("BU_ID: "+student.getBuId());
        notes = new JTextArea(student.getNotes(), NOTE_ROWS, NOTE_COLUMNS);

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                goToEditStudent((Student)btn.getContext());
            }
        };
        editStudent = new ContextButton("Edit Student", this.student);
        editStudent.addActionListener(al);

        //see all students
        allStudentsButton = new JButton("See all students");
        alAllStudents = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seeAllStudents();
            }
        };
        allStudentsButton.addActionListener(alAllStudents);

        // save notes for a student
        saveButton = new JButton("Save Notes");
        alSaveNotes = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveNotes();
            }
        };
        saveButton.addActionListener(alSaveNotes);
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel corePanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new GridLayout(courses.size() + 1,3));
        JPanel notePanel = new JPanel();

        // top level vertical container for two sub-panels
        GroupLayout coreLayout = new GroupLayout(corePanel);

        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup(CENTER)
                .addComponent(buID)
                .addComponent(email)
                .addComponent(graduateLevel)
                .addComponent(topPanel)
                .addComponent(bottomPanel)
                .addComponent(notePanel)
                .addComponent(saveButton)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(buID)
                .addComponent(email)
                .addComponent(graduateLevel)
                .addComponent(topPanel)
                .addComponent(bottomPanel)
                .addComponent(notePanel)
                .addComponent(saveButton)
        );

        corePanel.setLayout(coreLayout);

        GroupLayout topLayout = new GroupLayout(topPanel);
        topLayout.setHorizontalGroup(topLayout.createSequentialGroup()
                //.addComponent(name)
                //.addComponent(graduateLevel)
                .addComponent(editStudent)
                .addComponent(allStudentsButton)
        );

        topLayout.setVerticalGroup(topLayout.createParallelGroup(CENTER)
                //.addComponent(name)
                //.addComponent(graduateLevel)
                .addComponent(editStudent)
                .addComponent(allStudentsButton)
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
            bottomPanel.add(new Label(Double.toString(student.getGrade(section.getId())),JLabel.CENTER));

            // add an action listener to the group
            JLabel tempGroup = new JLabel(this.student.getGraduateLevel());
            bottomPanel.add(tempGroup);
        }

        // for student notes
        TitledBorder noteBorder = new TitledBorder("Student Notes");
        noteBorder.setTitleJustification(TitledBorder.CENTER);
        noteBorder.setTitlePosition(TitledBorder.TOP);
        notePanel.setBorder(noteBorder);
        notePanel.add(notes);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        TitledBorder studentBorder = new TitledBorder(student.getFullName());
        studentBorder.setTitleJustification(TitledBorder.CENTER);
        studentBorder.setTitlePosition(TitledBorder.TOP);
        corePanel.setBorder(studentBorder);
        pane.setLayout(new GridBagLayout());
        pane.add(corePanel);
    }

    private void goToCourse(Course course) {
        CourseView classes = new CourseView(course);
        classes.setVisible(true);
        end();
    }

    private void goToGroup(Group group) {
        GroupView groupView = new GroupView(group);
        groupView.setVisible(true);
        end();
    }

    private void goToEditStudent(Student student) {
        EditStudentView editView = new EditStudentView(student);
        editView.setVisible(true);
        end();
    }

    private void seeAllStudents() {
        AllStudentsView allStudentsView = new AllStudentsView();
        allStudentsView.setVisible(true);
        end();
    }

    private void saveNotes() {
        String newNotes = notes.getText();
        student.setNotes(newNotes);
        student.save();
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        end();
    }
}

package templates;

import models.Course;
import models.Student;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class EditStudentView extends View {
    private JTextField buId;
    private JTextField familyName;
    private JTextField firstName;
    private JTextField middleInitial;
    private JComboBox gradLevel;
    private JTextField email;
    private JButton submitButton;
    private Student student;
    private Course course;

    // constructor for creating a new student
    public EditStudentView() {
        this.student = new Student("BU ID","First","MI","Last","Grad Level","student email");
        setup(700, 400, "Add Student");
        createUIComponents();
        buildLayout();
    }

    // constructor for editing an existing student
    public EditStudentView(Student student) {
        this.student = student;
        setup(700, 400, "Edit Student");
        createUIComponents();
        buildLayout() ;
    }

    public EditStudentView(Course course) {
        this.student = new Student("BU ID","First","MI","Last","Grad Level","student email");
        this.course = course;
        this.student = new Student("BU ID", "First name", "Middle Name", "Last Name", "Graduate Level", "Email");
        setup(700, 400, "Add Student");
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToStudent();
            }
        };
        submitButton = new JButton("Submit");
        submitButton.addActionListener(al);


        familyName = new JTextField(student.getFamilyName());
        familyName.setPreferredSize(new Dimension(200,10));
        firstName = new JTextField(student.getFirstName());
        firstName.setPreferredSize(new Dimension(200,10));
        middleInitial = new JTextField(student.getMiddleInitial());
        middleInitial.setPreferredSize(new Dimension(20,10));


        buId = new JTextField(student.getBuId());
        buId.setPreferredSize(new Dimension(200,10));
        email = new JTextField(student.getEmail());
        email.setPreferredSize(new Dimension(200,10));

        String[] gradLevels = new String[] {"Undergraduate","Graduate"};
        gradLevel = new JComboBox(gradLevels);
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();

        // Group Layout helps us put things into a grid
        GroupLayout layout = new GroupLayout(panel);

        // creates gaps between edge of window and components
        layout.setAutoCreateContainerGaps(true);

        // the sequential groups are a set of elements that comprise a row or column.  By making the
        // horizontal group parallel, we are saying these should appear in the same column.  Parallel
        // Groups also handle alignment
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(firstName)
                .addComponent(middleInitial)
                .addComponent(familyName)
                .addComponent(buId)
                .addComponent(email)
                .addComponent(gradLevel)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(firstName)
                .addComponent(middleInitial)
                .addComponent(familyName)
                .addComponent(buId)
                .addComponent(email)
                .addComponent(gradLevel)
                .addComponent(submitButton)
        );

        panel.setLayout(layout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    private void goToStudent() {
//        this.student.setBuId(buId.getText());
//        this.student.setEmail(email.getText());
//        this.student.setGraduateLevel((String)gradLevel.getSelectedItem());
//        this.student.setFamilyName(familyName.getText());
        //this.student.save();
        //public Student(String buId, String first_name, String middle_name, String family_name, String graduateLevel, String email) {
        Student tempStudent = new Student(buId.getText(), firstName.getText(), middleInitial.getText(), familyName.getText(), gradLevel.getSelectedItem().toString(), email.getText());
        this.course.addStudent(tempStudent);

        StudentView studentView = new StudentView(tempStudent);
        studentView.setVisible(true);
        end();
    }
}

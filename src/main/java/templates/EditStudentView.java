package templates;

import models.Student;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class EditStudentView extends View {
    private JTextField buId;
    private JTextField name;
    private JComboBox gradLevel;
    private JTextField email;
    private JButton submitButton;
    private Student student;

    // constructor for creating a new student
    public EditStudentView() {
        this.student = new Student("BU ID","Student name","Grad Level","student email");
        setup(700, 400, "Add Student");
        createUIComponents();
        buildLayout() ;
    }

    // constructor for editing an existing student
    public EditStudentView(Student student) {
        this.student = student;
        setup(700, 400, "Edit Student");
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

        name = new JTextField(student.getName());
        name.setPreferredSize(new Dimension(200,10));
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
                .addComponent(name)
                .addComponent(buId)
                .addComponent(email)
                .addComponent(gradLevel)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(name)
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

    // TODO: this needs to take us to the newly created student
    private void goToStudent() {
        dispose();
    }
}

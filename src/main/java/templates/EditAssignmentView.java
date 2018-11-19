package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class EditAssignmentView extends View {
    //TODO change course ID to a label drawn off of the source view
    private JTextField courseId;
    private JTextField assignmentName;
    private JTextField assignmentType;
    private JTextField totalPoints;
    private JTextField description;
    private JButton submitButton;
    private Assignment assignment;

    // constructor for creating a new assignment
    public EditAssignmentView(Course course) {
        this.assignment = new Assignment(course.getSectionNumber(),"Assignment Name","Assignment Type",100);
        setup(1200, 800, "Add Assignment");
        createUIComponents();
        buildLayout() ;
    }

    // constructor for editing an existing assignment
    public EditAssignmentView(Assignment assignment) {
        this.assignment = assignment;
        setup(1200, 800, "Edit Assignment");
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

        assignmentName = new JTextField(assignment.getName());
        courseId = new JTextField(assignment.getCourse().getSectionNumber());
        assignmentType = new JTextField(assignment.getType());
        assignmentType.setMinimumSize(new Dimension(200,10));
        totalPoints = new JTextField(assignment.getTotalPoints());
        description = new JTextField(assignment.getDescription());

    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel spacerPanelV = new JPanel();
        spacerPanelV.add(Box.createVerticalStrut(10));

        // Group Layout helps us put things into a row or column
        GroupLayout layout = new GroupLayout(panel);

        // assembles everything into the parent grouping
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(submitButton)
        );

        panel.setLayout(layout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    // TODO: this needs to take us to the newly created assignment
    private void goToStudent() {
        dispose();
    }
}

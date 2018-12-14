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
    private JTextField assignmentName;
    private JTextField assignmentType;
    private JTextField totalPoints;
    private JLabel weightPrompt;
    private JTextField description;
    private JTextField weight;
    private JButton submitButton;
    private Assignment assignment;
    private Course course;
    private boolean create;

    // constructor for creating a new assignment
    public EditAssignmentView(Course course) {
        this.course = course;
        this.assignment = new Assignment(course.getId(), "Assignment Name", "Assignment Type", 100);
        this.create = true;

        setup(1200, 800, "Add Assignment");
        createUIComponents();
        buildLayout();
    }

    // constructor for editing an existing assignment
    public EditAssignmentView(Assignment assignment) {
        this.assignment = assignment;
        this.create = false;
        setup(1200, 800, "Edit Assignment");
        createUIComponents();
        buildLayout();
    }

    private void createUIComponents() {

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = assignmentName.getText();
                String desc = description.getText();
                String type = assignmentType.getText();
                String assignmentWeight = weight.getText();
                int points = Integer.parseInt(totalPoints.getText());

                if(create) {
                    createAssignment(name, type, desc, assignmentWeight, points);
                } else {
                    editAssignment();
                }
            }
        };
        submitButton = new JButton("Submit");
        submitButton.addActionListener(al);


        weight = new JTextField("1");

        assignmentName = new JTextField(assignment.getName());
        Course tempCourse = assignment.getCourse();
        assignmentType = new JTextField(assignment.getType());
        totalPoints = new JTextField(String.valueOf(assignment.getTotalPoints()));
        description = new JTextField((assignment.getDescription() == null ? "Description" : assignment.getDescription()));
        weightPrompt = new JLabel("Weight: ");

    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel weightPanel = new JPanel();
        JPanel spacerPanelV = new JPanel();
        spacerPanelV.add(Box.createVerticalStrut(12));

        // Group Layout helps us put things into a row or column
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout weightLayout = new GroupLayout(weightPanel);
        weightLayout.setAutoCreateContainerGaps(true);

        // assembles everything into the parent grouping
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(totalPoints)
                .addComponent(weightPanel)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(totalPoints)
                .addComponent(weightPanel)
                .addComponent(submitButton)
        );

        weightLayout.setVerticalGroup(weightLayout.createParallelGroup()
                .addComponent(weightPrompt)
                .addComponent(weight)
        );

        weightLayout.setHorizontalGroup(weightLayout.createSequentialGroup()
                .addComponent(weightPrompt)
                .addComponent(weight)
        );

        panel.setLayout(layout);
        weightPanel.setLayout(weightLayout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    private void createAssignment(String name, String type, String desc, String weight, int points) {
        float assignmentWeight = Float.parseFloat(weight);
        try{
            //creating a new assignment
            Assignment assignment = new Assignment(this.course.getId(), name, type, points);
            assignment.saveNew(this.course.getId());
            this.course.addAssignment(assignment);
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.assignment.save();
        CourseView editAssignmentView = new CourseView(this.course);
        editAssignmentView.setVisible(true);
        end();
    }

    private void editAssignment() {
        this.assignment.setDescription(description.getText());
        this.assignment.setName(assignmentName.getText());
        this.assignment.setType(assignmentType.getText());
        this.assignment.save();
        AssignmentView assignmentView = new AssignmentView(this.assignment);
        assignmentView.setVisible(true);
        end();
    }
}

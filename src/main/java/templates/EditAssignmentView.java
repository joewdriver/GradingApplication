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
    private JLabel ugradWeightPrompt, gradWeightPrompt, ugradWeightTypePrompt, gradWeightTypePrompt;
    private JTextField description;
    private JTextField ugradWeight, gradWeight, ugradWeightType, gradWeightType;
    private JButton submitButton;
    private Assignment assignment;
    private Course course;
    private boolean create;

    // constructor for creating a new assignment
    public EditAssignmentView(Course course) {
        this.course = course;
        this.assignment = new Assignment(course.getId(), "Assignment Name", "Assignment Type",
                100, 0.0, 0.0, 0.0, 0.0);
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
                String ugradAssignmentWeight = ugradWeight.getText();
                String gradAssignmentWeight = gradWeight.getText();
                String ugradAssignmentTypeWeight = ugradWeightType.getText();
                String gradAssignmentTypeWeight = gradWeightType.getText();
                int points = Integer.parseInt(totalPoints.getText());

                if(create) {
                    createAssignment(name, type, desc, ugradAssignmentWeight, gradAssignmentWeight,
                            ugradAssignmentTypeWeight, gradAssignmentTypeWeight, points);
                } else {
                    editAssignment();
                }
            }
        };
        submitButton = new JButton("Submit");
        submitButton.addActionListener(al);


        ugradWeight = new JTextField(Double.toString(assignment.getUgradWeight()));
        gradWeight = new JTextField(Double.toString(assignment.getGradWeight()));
        ugradWeightType = new JTextField(Double.toString(assignment.getUgradWeightType()));
        gradWeightType = new JTextField(Double.toString(assignment.getGradWeightType()));

        assignmentName = new JTextField(assignment.getName());
        Course tempCourse = assignment.getCourse();
        assignmentType = new JTextField(assignment.getType());
        totalPoints = new JTextField(String.valueOf(assignment.getTotalPoints()));
        description = new JTextField((assignment.getDescription() == null ? "Description" : assignment.getDescription()));
        ugradWeightPrompt = new JLabel("Ugrad. Weight: ");
        gradWeightPrompt = new JLabel("Grad. Weight: ");
        ugradWeightTypePrompt = new JLabel("Ugrad. Type Weight: ");
        gradWeightTypePrompt = new JLabel("Grad. Type Weight: ");
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel ugradWeightPanel = new JPanel();
        JPanel gradWeightPanel = new JPanel();
        JPanel ugradWeightTypePanel = new JPanel();
        JPanel gradWeightTypePanel = new JPanel();
        JPanel spacerPanelV = new JPanel();
        spacerPanelV.add(Box.createVerticalStrut(12));

        // Group Layout helps us put things into a row or column
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout ugradWeightLayout = new GroupLayout(ugradWeightPanel);
        GroupLayout gradWeightLayout = new GroupLayout(gradWeightPanel);
        GroupLayout ugradWeightTypeLayout = new GroupLayout(ugradWeightTypePanel);
        GroupLayout gradWeightTypeLayout = new GroupLayout(gradWeightTypePanel);
        ugradWeightLayout.setAutoCreateContainerGaps(true);
        gradWeightLayout.setAutoCreateContainerGaps(true);
        ugradWeightTypeLayout.setAutoCreateContainerGaps(true);
        gradWeightTypeLayout.setAutoCreateContainerGaps(true);

        // assembles everything into the parent grouping
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(totalPoints)
                .addComponent(ugradWeightPanel)
                .addComponent(gradWeightPanel)
                .addComponent(ugradWeightTypePanel)
                .addComponent(gradWeightTypePanel)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(assignmentName)
                .addComponent(assignmentType)
                .addComponent(description)
                .addComponent(totalPoints)
                .addComponent(ugradWeightPanel)
                .addComponent(gradWeightPanel)
                .addComponent(ugradWeightTypePanel)
                .addComponent(gradWeightTypePanel)
                .addComponent(submitButton)
        );

        ugradWeightLayout.setVerticalGroup(ugradWeightLayout.createParallelGroup()
                .addComponent(ugradWeightPrompt)
                .addComponent(ugradWeight)
        );

        ugradWeightLayout.setHorizontalGroup(ugradWeightLayout.createSequentialGroup()
                .addComponent(ugradWeightPrompt)
                .addComponent(ugradWeight)
        );

        gradWeightLayout.setVerticalGroup(gradWeightLayout.createParallelGroup()
                .addComponent(gradWeightPrompt)
                .addComponent(gradWeight)
        );

        gradWeightLayout.setHorizontalGroup(gradWeightLayout.createSequentialGroup()
                .addComponent(gradWeightPrompt)
                .addComponent(gradWeight)
        );

        ugradWeightTypeLayout.setVerticalGroup(ugradWeightTypeLayout.createParallelGroup()
                .addComponent(ugradWeightTypePrompt)
                .addComponent(ugradWeightType)
        );

        ugradWeightTypeLayout.setHorizontalGroup(ugradWeightTypeLayout.createSequentialGroup()
                .addComponent(ugradWeightTypePrompt)
                .addComponent(ugradWeightType)
        );

        gradWeightTypeLayout.setVerticalGroup(gradWeightTypeLayout.createParallelGroup()
                .addComponent(gradWeightTypePrompt)
                .addComponent(gradWeightType)
        );

        gradWeightTypeLayout.setHorizontalGroup(gradWeightTypeLayout.createSequentialGroup()
                .addComponent(gradWeightTypePrompt)
                .addComponent(gradWeightType)
        );

        panel.setLayout(layout);
        ugradWeightPanel.setLayout(ugradWeightLayout);
        gradWeightPanel.setLayout(gradWeightLayout);
        ugradWeightTypePanel.setLayout(ugradWeightTypeLayout);
        gradWeightTypePanel.setLayout(gradWeightTypeLayout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    private void createAssignment(String name, String type, String desc, String uWeight, String gWeight,
                                  String uWeightType, String gWeightType,
                                  int points) {
        double ugradAssignmentWeight = Double.parseDouble(uWeight);
        double gradAssignmentWeight = Double.parseDouble(gWeight);
        double ugradAssignmentTypeWeight = Double.parseDouble(uWeightType);
        double gradAssignmentTypeWeight = Double.parseDouble(gWeightType);
        int assignmentID = Assignment.getLastId() + 1;
        //TODO: implement the graduate weighting
        try{
            //creating a new assignment
            Assignment assignment = new Assignment(this.course.getId(), name, type, points, ugradAssignmentWeight,
                    gradAssignmentWeight, ugradAssignmentTypeWeight, gradAssignmentTypeWeight);
            assignment.saveNew(this.course.getId());
            this.course.addAssignment(assignment);
        } catch(Exception e) {
            e.printStackTrace();
            //updating an existing assignment
            //Course tempCourse = new Course(this.assignment.getClassId());
            //tempCourse.deleteAssignment(this.assignment);
            //Assignment assignment = new Assignment(this.assignment.getClassId(), name, type, points,
            //        ugradAssignmentWeight, gradAssignmentWeight, ugradAssignmentTypeWeight, gradAssignmentTypeWeight);
            //tempCourse.addAssignment(assignment, assignmentID);
            //assignment.save();
        }
        //this.assignment.save();
        CourseView editAssignmentView = new CourseView(this.course);
        editAssignmentView.setVisible(true);
        end();
    }

    private void editAssignment() {
        this.assignment.setDescription(description.getText());
        this.assignment.setName(assignmentName.getText());
        this.assignment.setType(assignmentType.getText());
        this.assignment.setUgradWeight(Double.parseDouble(ugradWeight.getText()));
        this.assignment.setGradWeight(Double.parseDouble(gradWeight.getText()));
        this.assignment.setUgradWeightType(Double.parseDouble(ugradWeightType.getText()));
        this.assignment.setGradWeightType(Double.parseDouble(gradWeightType.getText()));
        this.assignment.save();
        AssignmentView assignmentView = new AssignmentView(this.assignment);
        assignmentView.setVisible(true);
        end();
    }
}

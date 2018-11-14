package templates;

import models.Assignment;
import utils.ContextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class AssignmentView extends JFrame {
    private JButton editButton;
    private JButton courseInfo;
    private JLabel totalPoints;
    private JLabel assignmentName;
    private JLabel assignmentDescription;
    private Assignment assignment;
    private JLabel averageScore;
    private JLabel type;

    public AssignmentView(Assignment assignment) {
        this.assignment = assignment;
        setup();
    }

    public void setup() {
        this.createUIComponents();
        setTitle("Grading Records - Login");
        setSize(700,400);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        editButton = new JButton("Edit");
        ActionListener alEdit = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editAssignment();
            }
        };
        editButton.addActionListener(alEdit);

        courseInfo = new JButton(assignment.getCourse().getSectionNumber() + " " + assignment.getCourse().getName());
        ActionListener alCourse = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToCourse();
            }
        };
        courseInfo.addActionListener(alCourse);

        assignmentName = new JLabel(assignment.getName());
        assignmentDescription = new JLabel(assignment.getDescription());
        averageScore = new JLabel("Average Score: " + Double.toString(assignment.getAverageScore()));
        totalPoints = new JLabel(" / " + Integer.toString(assignment.getTotalPoints()));
        type = new JLabel(assignment.getType());
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel corePanel = new JPanel();
        JPanel pointPanel = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel spacerPanelH = new JPanel();
        JPanel spacerPanelV = new JPanel();

        spacerPanelH.add(Box.createHorizontalStrut(10));
        spacerPanelV.add(Box.createVerticalStrut(10));

        // Group Layout helps us put things into a grid
        GroupLayout coreLayout = new GroupLayout(corePanel);
        GroupLayout pointLayout = new GroupLayout(pointPanel);
        GroupLayout nameLayout = new GroupLayout(namePanel);

        // creates gaps between edge of window and components
        coreLayout.setAutoCreateContainerGaps(true);

        // the sequential groups are a set of elements that comprise a row or column.  By making the
        // horizontal group parallel, we are saying these should appear in the same column.  Parallel
        // Groups also handle alignment
        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup(CENTER)
                .addComponent(namePanel)
                .addComponent(courseInfo)
                .addComponent(pointPanel)
                .addComponent(spacerPanelV)
                .addComponent(assignmentDescription)
                .addComponent(spacerPanelV)
                .addComponent(editButton)
        );

        // by making the vertical group sequential, we order the items top to bottom
        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(namePanel)
                .addComponent(courseInfo)
                .addComponent(pointPanel)
                .addComponent(spacerPanelV)
                .addComponent(assignmentDescription)
                .addComponent(spacerPanelV)
                .addComponent(editButton)
        );

        nameLayout.setHorizontalGroup(nameLayout.createSequentialGroup()
                .addComponent(courseInfo)
                .addComponent(spacerPanelH)
                .addComponent(assignmentName)
        );

        nameLayout.setVerticalGroup(nameLayout.createParallelGroup(CENTER)
                .addComponent(courseInfo)
                .addComponent(spacerPanelH)
                .addComponent(assignmentName)
        );

        pointLayout.setHorizontalGroup(pointLayout.createSequentialGroup()
                .addComponent(averageScore)
                .addComponent(totalPoints)
        );

        pointLayout.setVerticalGroup(pointLayout.createParallelGroup(CENTER)
                .addComponent(averageScore)
                .addComponent(totalPoints)
        );

        corePanel.setLayout(coreLayout);
        pointPanel.setLayout(pointLayout);
        namePanel.setLayout(nameLayout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(corePanel);
    }

    /**
     * updates the form and resets it with the new object
     */
    public void editAssignment() {
        //TODO add transition to the edit view
    }

    public void goToCourse() {
        CourseView courseView = new CourseView(this.assignment.getCourse());
        courseView.setVisible(true);
        dispose();
    }
}

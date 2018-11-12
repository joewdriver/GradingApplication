package templates;

import models.Course;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class EditCourseView extends View {
    private JTextField courseName;
    private JTextField courseId;
    private JTextField year;
    private JLabel header;
    private String headerData;
    private JComboBox season;
    private JButton submitButton;
    private Course course;

    public EditCourseView() {
        this.course = new Course("Section","Course Name","Year");
        headerData = "Create a new Course";
        setup(700, 400, "Gradium - Add Course");
        createUIComponents();
        buildLayout() ;
    }

    public EditCourseView(Course course) {
        this.course=course;
        headerData = "Edit this Course";
        setup(700, 400, "Gradium - Edit Course");
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToCourse();
            }
        };
        submitButton = new JButton("Submit");
        submitButton.addActionListener(al);

        header = new JLabel(headerData);

        courseName = new JTextField(course.getName());
        courseName.setPreferredSize(new Dimension(200,10));
        courseId = new JTextField(course.getSectionNumber());
        courseId.setPreferredSize(new Dimension(200,10));
        year = new JTextField(course.getYear());
        year.setPreferredSize(new Dimension(40,30));

        // TODO: populate default value based on course being edited
        String[] seasons = new String[] {"Spring","Fall","Winter"};
        season = new JComboBox(seasons);
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel yearPanel = new JPanel();

        // Group Layout helps us put things into a grid
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout yearLayout = new GroupLayout(yearPanel);

        // creates gaps between edge of window and components
        layout.setAutoCreateContainerGaps(true);

        // the sequential groups are a set of elements that comprise a row or column.  By making the
        // horizontal group parallel, we are saying these should appear in the same column.  Parallel
        // Groups also handle alignment
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(header)
                .addComponent(courseName)
                .addComponent(courseId)
                .addComponent(yearPanel)
                .addComponent(submitButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(header)
                .addComponent(courseName)
                .addComponent(courseId)
                .addComponent(yearPanel)
                .addComponent(submitButton)
        );

        panel.setLayout(layout);

        yearLayout.setVerticalGroup(yearLayout.createParallelGroup(CENTER)
                .addComponent(season)
                .addComponent(year)
        );

        yearLayout.setHorizontalGroup(yearLayout.createSequentialGroup()
                .addComponent(season)
                .addComponent(year)
        );

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    // TODO: this needs to take us to the newly created course
    private void goToCourse() {
        dispose();
    }
}

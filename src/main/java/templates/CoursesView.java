package templates;

import models.Course;
import utils.ContextButton;
import utils.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.GroupLayout.Group;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CoursesView extends JFrame {
    private JButton newClassButton;
    private JLabel classPrompt;
    private ArrayList<Course> courses;
    private DBManager db = new DBManager();
    private ActionListener alCourseView;

    public CoursesView() {
        this.createUIComponents();
        setTitle("Grading Records - All Classes");
        setSize(700,400);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        //quick label for the header
        classPrompt = new JLabel("Please Choose a Course to View");

        //link to the new class form
        newClassButton = new JButton("Create a new Course");

        // we'll create an arraylist to hold our courses
        courses = db.getCourses();

        // providing a dummy list for the moment
        //TODO: replace with db call to populate the arraylist


        alCourseView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its context object to pass in.
                ContextButton btn = (ContextButton)e.getSource();
                goToCourse((Course)btn.getContext());
            }
        };
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel headerPanel =  new JPanel();

        // Group Layout helps us put things into a form
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout headerLayout = new GroupLayout(headerPanel);

        // creates gaps between edge of window and components
        layout.setAutoCreateContainerGaps(true);
        headerLayout.setAutoCreateContainerGaps(true);

        // these two groups will represent the header
        Group headerHorizontal = headerLayout.createSequentialGroup();
        Group headerVertical = headerLayout.createParallelGroup(CENTER);

        // the top level label and the button should appear in a single row
        headerHorizontal
                .addComponent(classPrompt)
                .addComponent(newClassButton);

        headerVertical
                .addComponent(classPrompt)
                .addComponent(newClassButton);

        // apply the groups to the header panel
        headerLayout.setVerticalGroup(headerVertical);
        headerLayout.setHorizontalGroup(headerHorizontal);

        // these two groups will handle the list of class buttons
        Group listHorizontal = layout.createParallelGroup(CENTER);
        Group listVertical = layout.createSequentialGroup();

        // populate the groups with our ui elements in order, with the header panel at the top
        listHorizontal.addComponent(headerPanel);
        listVertical.addComponent(headerPanel);
        for(Course course: courses) {
            ContextButton btn = new ContextButton(course.getName(), course);
            btn.addActionListener(alCourseView);
            listHorizontal.addComponent(btn);
            listVertical.addComponent(btn);
        }

        // now we apply the groups to the layout
        layout.setHorizontalGroup(listHorizontal);
        layout.setVerticalGroup(listVertical);

        panel.setLayout(layout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    /**
     * takes a class id and delivers it to the class view for rendering
     * @param course the course object
     */
    private void goToCourse(Course course) {

        CourseView classes = new CourseView(course);
        classes.setVisible(true);
        dispose();
    }
}

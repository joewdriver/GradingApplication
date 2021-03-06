package templates;

import models.Course;
import utils.ContextButton;
import utils.DBManager;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.GroupLayout.Group;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CoursesView extends View {
    private JButton newCourseButton, sortNewest, sortOldest, reset, search, sortActive;
    private JButton allStudentsButton;
    private JTextField searchField;
    private JLabel classPrompt;
    private ArrayList<Course> courses;
    private ActionListener alCourseView, alSort, alSearch, alActive, alAllStudents;
    private String sortCondition, searchTerm;
    private boolean active;

    public CoursesView() {
        setup(1200, 800, "Gradium All Classes");
        this.sortCondition = "";
        this.searchTerm = "";
        active = true;
        createUIComponents();
        buildLayout();
    }

    public CoursesView(String sortCondition) {
        setup(1200, 800, "Gradium All Classes");
        this.sortCondition = sortCondition;
        this.searchTerm = "";
        active = true;
        createUIComponents();
        buildLayout();
    }

    public CoursesView(String sortCondition, String searchTerm) {
        setup(1200, 800, "Gradium All Classes");
        this.sortCondition = sortCondition;
        this.searchTerm = searchTerm;
        this.active = true;
        createUIComponents();
        buildLayout();
    }

    public CoursesView(String sortCondition, String searchTerm, boolean active) {
        setup(1200, 800, "Gradium All Classes");
        this.sortCondition = sortCondition;
        this.searchTerm = searchTerm;
        this.active = active;
        createUIComponents();
        buildLayout();
    }



    private void createUIComponents() {

        //quick label for the header
        classPrompt = new JLabel("Please Choose a Course to View");

        //link to the new course form
        newCourseButton = new JButton("Create a new Course");
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        };
        newCourseButton.addActionListener(al);

        // button to only show active courses
        if(active) {
            sortActive = new JButton("Show all Courses");
        } else if (!active) {
            sortActive = new JButton("Show active Courses");
        }

        //see all students
        allStudentsButton = new JButton("See all students");
        alAllStudents = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seeAllStudents();
            }
        };
        allStudentsButton.addActionListener(alAllStudents);

        // set up the sort buttons
        sortNewest = new JButton("newest");
        sortOldest = new JButton("oldest");
        reset = new JButton("reset");
        search = new JButton("search");
        searchField = new JTextField(searchTerm);
        searchField.setPreferredSize(new Dimension(150,20));

        // we'll create an arraylist to hold our courses
        courses = db.getCourses();
        db.closeDB();

        if (sortCondition.equals("oldest")) {
            Collections.sort(courses);
        }
        if (sortCondition.equals("newest")) {
            Collections.sort(courses);
            Collections.reverse(courses);
        }

        alCourseView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its context object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToCourse((Course) btn.getContext());
            }
        };

        alSort = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text to pass in.
                JButton button = (JButton) e.getSource();
                sortCourses(button.getText());
            }
        };

        alSearch = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchCourses();
            }
        };

        alActive = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeCourses();
            }
        };

        sortNewest.addActionListener(alSort);
        sortOldest.addActionListener(alSort);
        reset.addActionListener(alSort);
        search.addActionListener(alSearch);
        sortActive.addActionListener(alActive);

    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel headerPanel =  new JPanel();
        JPanel sortPanel =  new JPanel();

        // Group Layout helps us put things into a form
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout headerLayout = new GroupLayout(headerPanel);
        GroupLayout sortLayout = new GroupLayout(sortPanel);

        // creates gaps between edge of window and components
        layout.setAutoCreateContainerGaps(true);
        headerLayout.setAutoCreateContainerGaps(true);
        sortLayout.setAutoCreateContainerGaps(true);

        // these two groups will represent the header
        Group headerHorizontal = headerLayout.createSequentialGroup();
        Group headerVertical = headerLayout.createParallelGroup(CENTER);

        // the top level label and the button should appear in a single row
        headerHorizontal
                .addComponent(classPrompt)
                .addComponent(newCourseButton)
                .addComponent(allStudentsButton);

        headerVertical
                .addComponent(classPrompt)
                .addComponent(newCourseButton)
                .addComponent(allStudentsButton);

        Group sortHorizontal = headerLayout.createSequentialGroup();
        Group sortVertical = headerLayout.createParallelGroup(CENTER);

        sortHorizontal
                .addComponent(sortNewest)
                .addComponent(sortOldest)
                .addComponent(search)
                .addComponent(searchField)
                .addComponent(reset);

        sortVertical
                .addComponent(sortNewest)
                .addComponent(sortOldest)
                .addComponent(search)
                .addComponent(searchField)
                .addComponent(reset);

        // apply the groups to the header panel
        headerLayout.setVerticalGroup(headerVertical);
        headerLayout.setHorizontalGroup(headerHorizontal);

        // these two groups will handle the list of class buttons
        Group listHorizontal = layout.createParallelGroup(CENTER);
        Group listVertical = layout.createSequentialGroup();

        // populate the groups with our ui elements in order, with the header panel at the top
        listHorizontal.addComponent(headerPanel);
        listVertical.addComponent(headerPanel);
        listHorizontal.addComponent(sortPanel);
        listVertical.addComponent(sortPanel);
        listHorizontal.addComponent(sortActive);
        listVertical.addComponent(sortActive);

        for(Course course: courses) {
            // first check if the course is active or if we require active courses.
            if(course.getActive() || !this.active) {
                // if we are using a search term, verify the course qualifies before adding it in
                if (course.getName().contains(searchTerm) || course.getSectionNumber().contains(searchTerm)) {
                    // add the course object to a dynamically generated context button
                    ContextButton btn = new ContextButton(
                            course.getSectionNumber() + " " + course.getName() +
                                    " " + course.getSeason() + " " + course.getYear(), course);
                    btn.addActionListener(alCourseView);
                    listHorizontal.addComponent(btn);
                    listVertical.addComponent(btn);
                }
            }
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
        end();
    }

    private void sortCourses(String sort) {
        CoursesView coursesView = new CoursesView(sort);
        coursesView.setVisible(true);
        end();
    }

    private void searchCourses() {
        CoursesView coursesView = new CoursesView(sortCondition, searchField.getText());
        coursesView.setVisible(true);
        end();
    }

    private void addCourse() {
        EditCourseView editCourse = new EditCourseView();
        editCourse.setVisible(true);
        end();
    }

    private void activeCourses() {
        CoursesView coursesView = new CoursesView(sortCondition, searchField.getText(), !active);
        coursesView.setVisible(true);
        end();
    }

    private void seeAllStudents() {
        AllStudentsView allStudentsView = new AllStudentsView();
        allStudentsView.setVisible(true);
        end();
    }
}

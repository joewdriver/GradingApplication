package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.ContextButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CourseView extends JFrame {
    private ContextButton addStudentButton;
    private ContextButton importStudentsButton;
    private ContextButton editClassSettings;
    private JButton saveButton;
    private JButton viewAllCoursesButton;
    private JLabel classNameHeader;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students = new ArrayList<Student>();
    private Course course;
    private ActionListener alStudentView;
    private ActionListener alAssignmentView;
    private ActionListener alSave;
    private ActionListener alSettings;
    private ActionListener alAddStudent;
    private ActionListener alImportStudents;
    private ActionListener alViewAllCourses;



    public CourseView(Course course) {
        this.course = course;
        this.createUIComponents();
        setTitle("Grading Records - " + course.getName());

        // going to need alot more space with this one
        setSize(1200,800);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        assignments = course.getAssignments();
        students = course.getStudents();

        classNameHeader = new JLabel("Editing grades for " + course.getName());

        saveButton = new JButton("Save");
        alSave = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                save();
            }
        };

        // action listener for the row headers
        alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToStudent((Student)btn.getContext());
            }
        };

        // action listener for the column headers
        alAssignmentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToAssignment((Assignment)btn.getContext());
            }
        };

        // action listener for the row headers
        alStudentView = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                ContextButton btn = (ContextButton) e.getSource();
                goToStudent((Student)btn.getContext());
            }
        };

        alSettings = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                goToSettings((Course)btn.getContext());
            }
        };

        alAddStudent = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                addStudent((Course)btn.getContext());
            }
        };

        alImportStudents = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                importStudents((Course)btn.getContext());
            }
        };

        alViewAllCourses = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllCourses();
            }
        };


        addStudentButton = new ContextButton("Add A Student", this.course);
        editClassSettings = new ContextButton("Class Settings", this.course);
        importStudentsButton = new ContextButton("Import Student List", this.course);
        viewAllCoursesButton = new JButton("View all Courses");

        addStudentButton.addActionListener(alAddStudent);
        editClassSettings.addActionListener(alSettings);
        importStudentsButton.addActionListener(alImportStudents);
        viewAllCoursesButton.addActionListener(alViewAllCourses);
    }

    /**
     * our most complicated view.  To get this going we're going to have a GridLayout and two GroupLayouts
     * embedded into a third GroupLayout, all within containing panels
     */
    private void buildLayout() {
        // temp lists for graduates and undergraduates
        ArrayList<Student> graduates = new ArrayList<Student>();
        ArrayList<Student> undergraduates = new ArrayList<Student>();

        // populate each list accordingly
        for(Student student : students) {
            if(student.getGraduateLevel().equals("Undergrad"))
                undergraduates.add(student);
            else
                graduates.add(student);
        }

        // this is the overall parent
        Container pane = getContentPane();

        JPanel framePanel = new JPanel();
        JPanel headerPanel =  new JPanel();
        JPanel footerPanel =  new JPanel();
        JPanel spacePanel =  new JPanel();
        //JPanel undergraduatePanel = new JPanel(new GridLayout(undergraduates.size() + 3,assignments.size() + 1));
        JPanel undergraduatePanel = new JPanel(new GridLayout(undergraduates.size()+4,assignments.size()+3));
        JPanel graduatePanel = new JPanel(new GridLayout(graduates.size() + 4,assignments.size() + 3));

        // core overall layout vertical
        GroupLayout coreLayout = new GroupLayout(framePanel);
        coreLayout.setAutoCreateContainerGaps(true);

        // core layout grouping to order our member panels high to low
        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(headerPanel)
                .addComponent(undergraduatePanel)
                .addComponent(spacePanel)
                .addComponent(graduatePanel)
                .addComponent(footerPanel)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(undergraduatePanel)
                .addComponent(spacePanel)
                .addComponent(graduatePanel)
                .addComponent(footerPanel)
        );

        framePanel.setLayout(coreLayout);

        // horizontal header layout.  Currently has a single item, but built to be flexible for additions
        GroupLayout headerLayout = new GroupLayout(headerPanel);

        headerLayout.setVerticalGroup(headerLayout.createParallelGroup(CENTER)
                .addComponent(classNameHeader)
                .addComponent(viewAllCoursesButton));

        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(classNameHeader)
                .addComponent(viewAllCoursesButton));


        // Undergrad set-up
        // grid layout organizes itself into one long array, a total pain, so we have to add everything sequentially
        TitledBorder ugradBorder = new TitledBorder("Undergraduates");
        ugradBorder.setTitleJustification(TitledBorder.CENTER);
        ugradBorder.setTitlePosition(TitledBorder.TOP);
        undergraduatePanel.setBorder(ugradBorder);

        // empty top left corner
        // Add in the assignment weights
        undergraduatePanel.add(new JLabel(""));
        String tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getAssignmentType().compareTo(tmpName) != 0) {
                undergraduatePanel.add(new JLabel(assignments.get(i).getAssignmentType()));
                tmpName = assignments.get(i).getAssignmentType();
            } else
                undergraduatePanel.add(new JLabel(""));
        }
        undergraduatePanel.add(new JLabel(""));

        undergraduatePanel.add(new JLabel(""));
        tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getAssignmentType().compareTo(tmpName) != 0) {
                undergraduatePanel.add(new TextField("100"));
                tmpName = assignments.get(i).getAssignmentType();
            } else
                undergraduatePanel.add(new JLabel(""));
        }
        undergraduatePanel.add(new JLabel(""));

        // next the assignment list in the top row
        undergraduatePanel.add(new JLabel(""));
        for(Assignment assignment:assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignmentView);
            undergraduatePanel.add(btn);
        }
        undergraduatePanel.add(new JLabel(""));
        undergraduatePanel.add(new JLabel(""));

        // next the assignment list in the top row
        for(Assignment assignment:assignments) {
            undergraduatePanel.add(new TextField("100"));
        }
        undergraduatePanel.add(new JLabel("  Total Grade"));

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(Student student: undergraduates) {
            System.out.println(student.getGraduateLevel());
            ContextButton btn = new ContextButton(student.getName(), student);
            btn.addActionListener(this.alStudentView);
            undergraduatePanel.add(btn);
            //TODO: add the average calculation here based on db call
            //undergraduatePanel.add(new TextField("100"));
            for(Assignment assignment:assignments) {
                // TODO: resolve this based on db call of student assignment join
                undergraduatePanel.add(new TextField("100"));
            }
            undergraduatePanel.add(new TextField("100"));
        }

        // adding in the graduate stuff
        TitledBorder gradBorder = new TitledBorder("Graduates");
        gradBorder.setTitleJustification(TitledBorder.CENTER);
        gradBorder.setTitlePosition(TitledBorder.TOP);
        graduatePanel.setBorder(gradBorder);

        graduatePanel.add(new JLabel(""));
        tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getAssignmentType().compareTo(tmpName) != 0) {
                graduatePanel.add(new JLabel(assignments.get(i).getAssignmentType()));
                tmpName = assignments.get(i).getAssignmentType();
            } else
                graduatePanel.add(new JLabel(""));
        }
        graduatePanel.add(new JLabel(""));

        graduatePanel.add(new JLabel(""));
        tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getAssignmentType().compareTo(tmpName) != 0) {
                graduatePanel.add(new TextField("100"));
                tmpName = assignments.get(i).getAssignmentType();
            } else
                graduatePanel.add(new JLabel(""));
        }
        graduatePanel.add(new JLabel(""));

        // next the assignment list in the top row
        graduatePanel.add(new JLabel(""));
        for(Assignment assignment:assignments) {
            ContextButton btn = new ContextButton(assignment.getName(), assignment);
            btn.addActionListener(alAssignmentView);
            graduatePanel.add(btn);
        }
        graduatePanel.add(new JLabel(""));
        graduatePanel.add(new JLabel(""));

        // next the assignment list in the top row
        for(Assignment assignment:assignments) {
            graduatePanel.add(new TextField("100"));
        }
        graduatePanel.add(new JLabel("  Total Grade"));

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(Student student: graduates) {
            System.out.println(student.getGraduateLevel());
            ContextButton btn = new ContextButton(student.getName(), student);
            btn.addActionListener(this.alStudentView);
            graduatePanel.add(btn);
            //TODO: add the average calculation here based on db call
            //graduatePanel.add(new TextField("100"));
            for(Assignment assignment:assignments) {
                // TODO: resolve this based on db call of student assignment join
                graduatePanel.add(new TextField("100"));
            }
            graduatePanel.add(new TextField("100"));
        }

        // footer layout for various functional buttons
        GroupLayout footerLayout = new GroupLayout(footerPanel);

        footerLayout.setHorizontalGroup(footerLayout.createParallelGroup(CENTER)
                .addComponent(editClassSettings)
                .addComponent(addStudentButton)
                .addComponent(importStudentsButton));

        footerLayout.setHorizontalGroup(footerLayout.createSequentialGroup()
                .addComponent(editClassSettings)
                .addComponent(addStudentButton)
                .addComponent(importStudentsButton)
                .addComponent(saveButton));

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(framePanel);
    }

    private void goToSettings(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void addStudent(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void importStudents(Course course) {
        //TODO: install view transition here
        System.exit(0);
    }

    private void save() {
        //TODO: save function needs to read the scores, update the db, then reload the app
        CourseView courseView = new CourseView(this.course);
        courseView.setVisible(true);
        dispose();
    }

    private void goToStudent(Student student) {
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        dispose();
    }

    private void goToAssignment(Assignment assignment) {
        //TODO: add assignment view transition here
    }

    private void viewAllCourses() {
        CoursesView coursesView = new CoursesView();
        coursesView.setVisible(true);
        dispose();
    }
}

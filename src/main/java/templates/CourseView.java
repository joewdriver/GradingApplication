package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.ContextButton;
import utils.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.File;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CourseView extends View {
    private ContextButton addStudentButton;
    private ContextButton importStudentsButton;
    private ContextButton editClassSettings;
    private JButton closeButton;
    private JButton deleteButton;
    private JButton viewAllCoursesButton;
    private JButton addAssignment;
    private JLabel classNameHeader;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students = new ArrayList<Student>();
    private Course course;
    private ActionListener alStudentView;
    private ActionListener alAssignmentView;
    private ActionListener alClose;
    private ActionListener alDelete;
    private ActionListener alSettings;
    private ActionListener alAddStudent;
    private ActionListener alImportStudents;
    private ActionListener alViewAllCourses;
    private ActionListener alAddAssignment;



    public CourseView(Course course) {
        this.course = course;
        setup(1200, 800, "Gradium Records " + course.getName());
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {

        assignments = course.getAssignments();
        students = course.getStudents();

        if (this.course.getActive()) {
            classNameHeader = new JLabel("Viewing grades for " + course.getName());
        } else {
            classNameHeader = new JLabel("Viewing grades for " + course.getName() + " -- COURSE CLOSED");
        }

        closeButton = new JButton("Close Course");
        alClose = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("checkpoint 1");
                closeCourse();
            }
        };

        deleteButton = new JButton("Delete");
        alDelete = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete();
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

        alAddAssignment = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAssignment(course);
            }
        };


        addStudentButton = new ContextButton("Add A Student", this.course);
        editClassSettings = new ContextButton("Class Settings", this.course);
        importStudentsButton = new ContextButton("Import Student List", this.course);
        viewAllCoursesButton = new JButton("View all Courses");
        addAssignment = new JButton("Add Assignment");

        addStudentButton.addActionListener(alAddStudent);
        editClassSettings.addActionListener(alSettings);
        importStudentsButton.addActionListener(alImportStudents);
        viewAllCoursesButton.addActionListener(alViewAllCourses);
        addAssignment.addActionListener(alAddAssignment);
        deleteButton.addActionListener(alDelete);
        closeButton.addActionListener(alClose);
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
            if (assignments.get(i).getType().compareTo(tmpName) != 0) {
                undergraduatePanel.add(new JLabel(assignments.get(i).getType()));
                tmpName = assignments.get(i).getType();
            } else
                undergraduatePanel.add(new JLabel(""));
        }
        undergraduatePanel.add(new JLabel(""));

        undergraduatePanel.add(new JLabel(""));
        tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getType().compareTo(tmpName) != 0) {
                //TODO dynamically pull the weights
                undergraduatePanel.add(new TextField("100"));
                tmpName = assignments.get(i).getType();
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
            ContextButton btn = new ContextButton(student.getFullName(), student);

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
            if (assignments.get(i).getType().compareTo(tmpName) != 0) {
                graduatePanel.add(new JLabel(assignments.get(i).getType()));
                tmpName = assignments.get(i).getType();
            } else
                graduatePanel.add(new JLabel(""));
        }
        graduatePanel.add(new JLabel(""));

        graduatePanel.add(new JLabel(""));
        tmpName = "";
        for(int i=0;i<assignments.size();i++) {
            if (assignments.get(i).getType().compareTo(tmpName) != 0) {
                graduatePanel.add(new TextField("100"));
                tmpName = assignments.get(i).getType();
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
            ContextButton btn = new ContextButton(student.getFullName(), student);
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

        footerLayout.setVerticalGroup(footerLayout.createParallelGroup(CENTER)
                .addComponent(editClassSettings)
                .addComponent(addStudentButton)
                .addComponent(importStudentsButton)
                .addComponent(addAssignment)
                .addComponent(closeButton)
                .addComponent(deleteButton));

        footerLayout.setHorizontalGroup(footerLayout.createSequentialGroup()
                .addComponent(editClassSettings)
                .addComponent(addStudentButton)
                .addComponent(importStudentsButton)
                .addComponent(addAssignment)
                .addComponent(closeButton)
                .addComponent(deleteButton));


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
        EditStudentView editStudentView = new EditStudentView(course);
        editStudentView.setVisible(true);
        end();
    }

    private void importStudents(Course course) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv", "csv");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
        fileChooser.showDialog(null,"Please Select the File");
        fileChooser.setVisible(true);
        File filename = fileChooser.getSelectedFile();
        if(filename != null)
            System.out.println("File name "+filename.getName());
    }

    private void closeCourse() {
        System.out.println("checkpoint 2");
        this.course.setActive(0);
        this.course.save();
        CoursesView coursesView = new CoursesView();
        coursesView.setVisible(true);
        end();
    }

    private void delete(){
        this.course.deleteClass();

        CoursesView coursesView = new CoursesView();
        coursesView.setVisible(true);
        end();
    }

    private void goToStudent(Student student) {
        StudentView studentView = new StudentView(student);
        studentView.setVisible(true);
        end();
    }

    private void goToAssignment(Assignment assignment) {
//        AssignmentView assignmentView = new AssignmentView(assignment);
//        assignmentView.setVisible(true);
        EditAssignmentView editAssignmentView = new EditAssignmentView(assignment);
        editAssignmentView.setVisible(true);
        end();
    }

    private void viewAllCourses() {
        CoursesView coursesView = new CoursesView();
        coursesView.setVisible(true);
        end();
    }

    private void addAssignment(Course course) {
        EditAssignmentView editAssignmentView = new EditAssignmentView(course);
        editAssignmentView.setVisible(true);
        end();
    }

//    private void editAssignment() {
//        EditAssignmentView editAssignmentView = new EditAssignmentView();
//        editAssignmentView.setVisible(true);
//        end();
//    }
}

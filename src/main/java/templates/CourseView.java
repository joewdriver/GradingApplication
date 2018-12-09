package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.ContextButton;
import utils.View;

import javax.naming.Context;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class CourseView extends View {
    private ContextButton addStudentButton;
    private ContextButton importStudentsButton;
    private ContextButton cloneCourse;
    private JButton closeButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton viewAllCoursesButton;
    private JButton addAssignment;
    private JLabel classNameHeader, meanScore, medianScore, highScore, lowScore;
    private ArrayList<Assignment> assignments;
    private ArrayList<Student> students = new ArrayList<Student>();
    private Course course;
    private ActionListener alStudentView;
    private ActionListener alAssignmentView;
    private ActionListener alClose;
    private ActionListener alDelete;
    private ActionListener alClone;
    private ActionListener alEdit;
    private ActionListener alAddStudent;
    private ActionListener alImportStudents;
    private ActionListener alViewAllCourses;
    private ActionListener alAddAssignment;

    public CourseView(Course course) {
        this.course = course;
        try {
            setup(1200, 800, "Gradium Records " + course.getName());
        }catch (Exception e){
            setup(1200, 800, "Gradium Records ");
        }
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
                closeCourse();
            }
        };

        deleteButton = new JButton("Delete");
        alDelete = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        };

        editButton = new JButton("Edit Class");
        alEdit = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editClass();
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

        alClone = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ContextButton btn = (ContextButton) e.getSource();
                cloneCourse();
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
        cloneCourse = new ContextButton("Clone Course", this.course);
        importStudentsButton = new ContextButton("Import Student List", this.course);
        viewAllCoursesButton = new JButton("View all Courses");
        addAssignment = new JButton("Add Assignment");

        addStudentButton.addActionListener(alAddStudent);
        cloneCourse.addActionListener(alClone);
        importStudentsButton.addActionListener(alImportStudents);
        viewAllCoursesButton.addActionListener(alViewAllCourses);
        addAssignment.addActionListener(alAddAssignment);
        deleteButton.addActionListener(alDelete);
        closeButton.addActionListener(alClose);
        editButton.addActionListener(alEdit);

        meanScore = new JLabel("Mean Acore: " + course.getMeanScore());
        medianScore = new JLabel("Average Score: " + course.getMedianScore());
        highScore = new JLabel("High Score: " + course.getHighScore());
        lowScore = new JLabel("Low Score: " + course.getLowScore());
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
            System.out.println("email: " + student.getEmail());
            if(student.getGraduateLevel().equals("Undergraduate")) {
                undergraduates.add(student);
            } else {
                graduates.add(student);
            }
        }

        // this is the overall parent
        Container pane = getContentPane();

        // our vertically stored children
        JPanel framePanel = new JPanel();
        JPanel headerPanel =  new JPanel();
        JPanel footerPanel =  new JPanel();
        JPanel spacePanel =  new JPanel();
        JPanel statsPanel = new JPanel();

        //JPanel undergraduatePanel = new JPanel(new GridLayout(undergraduates.size() + 3,assignments.size() + 1));
        JPanel undergraduatePanel = new JPanel(new GridLayout(undergraduates.size()+4,assignments.size()+3));
        JPanel graduatePanel = new JPanel(new GridLayout(graduates.size() + 4,assignments.size() + 3));

        // core overall layout vertical
        GroupLayout coreLayout = new GroupLayout(framePanel);
        coreLayout.setAutoCreateContainerGaps(true);

        // core layout grouping to order our member panels high to low
        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(headerPanel)
                .addComponent(statsPanel)
                .addComponent(undergraduatePanel)
                .addComponent(spacePanel)
                .addComponent(graduatePanel)
                .addComponent(footerPanel)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(statsPanel)
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
                .addComponent(editButton)
                .addComponent(viewAllCoursesButton));

        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(classNameHeader)
                .addComponent(editButton)
                .addComponent(viewAllCoursesButton));

        GroupLayout statLayout = new GroupLayout(statsPanel);

        statLayout.setHorizontalGroup(statLayout.createSequentialGroup()
                .addComponent(meanScore)
                .addComponent(medianScore)
                .addComponent(highScore)
                .addComponent(lowScore)
        );

        statLayout.setVerticalGroup(statLayout.createParallelGroup(CENTER)
                .addComponent(meanScore)
                .addComponent(medianScore)
                .addComponent(highScore)
                .addComponent(lowScore)
        );


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
                undergraduatePanel.add(new JLabel("100"));
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
        undergraduatePanel.add(new JLabel("Total Points"));
        undergraduatePanel.add(new JLabel("Out Of:"));

        // next the assignment list in the top row
        int sum = 0;
        for(Assignment assignment:assignments) {
            int totalPoints = assignment.getTotalPoints();
            undergraduatePanel.add(new JLabel(Integer.toString(totalPoints)));
            sum += totalPoints;
        }
        undergraduatePanel.add(new JLabel(Integer.toString(sum)));

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work

        
        for(Student student: undergraduates) {
            System.out.println(student.getGraduateLevel());
            ContextButton btn = new ContextButton(student.getFullName(), student);
            if(!student.getNotes().equals("")) {
                btn.setForeground(Color.RED);
            }

            btn.addActionListener(this.alStudentView);
            undergraduatePanel.add(btn);
            //TODO: add the average calculation here based on db call
            //undergraduatePanel.add(new TextField("100"));
            double totalScore = 0.0;
            for(Assignment assignment:assignments) {
                undergraduatePanel.add(new JLabel(Double.toString(assignment.getScore(student))));
                totalScore += assignment.getScore(student);
            }
            undergraduatePanel.add(new JLabel(Double.toString(totalScore)));
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
                graduatePanel.add(new JLabel(Integer.toString(assignments.get(i).getTotalPoints())));
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
        graduatePanel.add(new JLabel("Total Points"));
        graduatePanel.add(new JLabel("Out Of"));

        // next the assignment list in the top row
        sum = 0;
        for(Assignment assignment:assignments) {
            int totalPoints = assignment.getTotalPoints();
            graduatePanel.add(new JLabel(Integer.toString(totalPoints)));
            sum += totalPoints;
        }
        graduatePanel.add(new JLabel(Integer.toString(sum)));

        // now we get weird. leftmost column should be name buttons, everything else text fields.
        // will need a nested loop to make this work
        for(Student student: graduates) {
            System.out.println(student.getGraduateLevel());
            ContextButton btn = new ContextButton(student.getFullName(), student);
            if(!student.getNotes().equals("")) {
                btn.setForeground(Color.RED);
            }

            btn.addActionListener(this.alStudentView);
            graduatePanel.add(btn);
            double totalScore = 0.0;
            for(Assignment assignment:assignments) {
                double score = assignment.getScore(student);
                graduatePanel.add(new JLabel(Double.toString(score)));
                totalScore = totalScore + score;
            }
            graduatePanel.add(new JLabel(Double.toString(totalScore)));
            // TODO resolve average grade
            //graduatePanel.add(new JLabel(Double.toString(student.getGrade(this.course.getSectionNumber()))));
        }

        // footer layout for various functional buttons
        GroupLayout footerLayout = new GroupLayout(footerPanel);

        footerLayout.setVerticalGroup(footerLayout.createParallelGroup(CENTER)
                .addComponent(cloneCourse)
                .addComponent(addStudentButton)
                .addComponent(importStudentsButton)
                .addComponent(addAssignment)
                .addComponent(closeButton)
                .addComponent(deleteButton));

        footerLayout.setHorizontalGroup(footerLayout.createSequentialGroup()
                .addComponent(cloneCourse)
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

    private void cloneCourse() {
        Course newCourse = this.course.cloneCourse();
        CourseView courseView = new CourseView(newCourse);
        courseView.setVisible(true);
        dispose();
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
        File studentList = fileChooser.getSelectedFile();
        if(studentList != null)
            System.out.println("File name "+studentList.getName());

        // Now let's parse the file
        // we expect to input to be as such: BU ID, first name, middle initial, last name
        // graduate level, email
        try {
            Scanner scanner = new Scanner(studentList);
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] data = str.split(",");
                if (data.length == 6) {
                    //TODO: this is harcoded, make it better
                    String buId = data[0];
                    String firstName = data[1];
                    String middleInitial = data[2];
                    String familyName = data[3];
                    String graduateLevel = data[4];
                    String email = data[5];

                    Student student = new Student(buId, firstName, middleInitial, familyName,
                            graduateLevel, email);

                    course.addStudent(student);
                    ArrayList<Course> list = student.getClasses();
                    for (Course blah : list) {
                        System.out.println(blah.getName());
                    }
                    System.out.println("Print here");
                } else {
                    System.out.println("you're wrong");
                }

            }
            scanner.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        CourseView classes = new CourseView(course);
        classes.setVisible(true);
        end();
    }

    private void closeCourse() {
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
        AssignmentView assignmentView = new AssignmentView(assignment);
        assignmentView.setVisible(true);
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

    private void editClass() {
        EditCourseView editCourseView = new EditCourseView(course);
        editCourseView.setVisible(true);
        end();
    }

//    private void editAssignment() {
//        EditAssignmentView editAssignmentView = new EditAssignmentView();
//        editAssignmentView.setVisible(true);
//        end();
//    }
}

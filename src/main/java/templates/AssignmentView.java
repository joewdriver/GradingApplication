package templates;

import models.Assignment;
import models.Course;
import models.Student;
import utils.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import utils.ContextField;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class AssignmentView extends View {
    private JLabel courseId;
    private JLabel assignmentName;
    private JLabel assignmentType;
    private JLabel totalPoints;
    private JLabel averageScore;
    private JLabel description;
    private JButton editButton;
    private JButton saveButton;
    private JButton backButton;
    private ArrayList<ContextField> contextFields;

    private Assignment assignment;

    // constructor for creating a new assignment
    public AssignmentView() {
        this.contextFields = new ArrayList<ContextField>();
        this.assignment = new Assignment(0,"Assignment Name","Assignment Type",100);
        setup(1200, 800, "Add Assignment");
        createUIComponents();
        buildLayout() ;
    }

    // constructor for editing an existing assignment
    public AssignmentView(Assignment assignment) {
        this.contextFields = new ArrayList<ContextField>();
        this.assignment = assignment;
        setup(1200, 800, "Edit Assignment");
        createUIComponents();
        buildLayout() ;
    }

    private void createUIComponents() {

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editAssignment();
            }
        };
        editButton = new JButton("Edit this assignment");
        editButton.addActionListener(al);

        ActionListener saveAl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAssignment();
            }
        };
        saveButton = new JButton("Save this assignment");
        saveButton.addActionListener(saveAl);

        ActionListener backAl = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                back();
            }
        };
        backButton = new JButton("Back");
        backButton.addActionListener(backAl);


        assignmentName = new JLabel(assignment.getName());
        courseId = new JLabel(assignment.getCourse().getSectionNumber());
        //courseId = new JLabel("111");
        assignmentType = new JLabel(assignment.getType());
        assignmentType.setMinimumSize(new Dimension(200,10));
        totalPoints = new JLabel(Integer.toString(assignment.getTotalPoints()));
        averageScore = new JLabel(Double.toString(assignment.getAverageScore()));
        description = new JLabel(assignment.getDescription());
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();
        JPanel headerPanel = new JPanel();
        JPanel scorePanel = new JPanel();
        JPanel spacerPanelH = new JPanel();
        spacerPanelH.add(Box.createHorizontalStrut(10));

        // Group Layout helps us put things into a row or column
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout headerLayout = new GroupLayout(headerPanel);
        GroupLayout scoreLayout = new GroupLayout(scorePanel);

        // creates gaps between edge of window and components
//        layout.setAutoCreateContainerGaps(true);
        headerLayout.setAutoCreateContainerGaps(true);

        headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
                .addComponent(courseId)
                .addComponent(spacerPanelH)
                .addComponent(assignmentName)
                .addComponent(spacerPanelH)
                .addComponent(assignmentType)
        );
        // Course ID and assignment header in the same row
        headerLayout.setVerticalGroup(headerLayout.createParallelGroup(CENTER)
                .addComponent(courseId)
                .addComponent(spacerPanelH)
                .addComponent(assignmentName)
                .addComponent(spacerPanelH)
                .addComponent(assignmentType)
        );


        // score layout is going to contain a variable number of sub-panels depending on the number of records we
        // intend to display -- defaulting to 5.
        GroupLayout.SequentialGroup seqScoreGroup = scoreLayout.createSequentialGroup();
        GroupLayout.ParallelGroup parScoreGroup = scoreLayout.createParallelGroup();

        //  also create temporary holders to assign to the score layout
        JPanel tmpPanel = new JPanel();
        tmpPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        GroupLayout tmpLayout = new GroupLayout(tmpPanel);
        GroupLayout.SequentialGroup tmpSeqScoreGroup = tmpLayout.createSequentialGroup();
        GroupLayout.ParallelGroup tmpParScoreGroup = tmpLayout.createParallelGroup();

        // loop through all students in the course
        int i = 0;
        Object temp = assignment.getCourse();
        if(temp != null){
            ArrayList<Student> students = assignment.getCourse().getStudents();
            for(Student student: students) {
                // add a new column after the fifth entry
                if(i >= 5) {
                    // each fifth iteration we assign the temporary containers to the score layout
                    tmpLayout.setVerticalGroup(tmpSeqScoreGroup);
                    tmpLayout.setHorizontalGroup(tmpParScoreGroup);
                    tmpPanel.setLayout(tmpLayout);
                    seqScoreGroup.addComponent(tmpPanel);
                    parScoreGroup.addComponent(tmpPanel);
                    seqScoreGroup.addComponent(spacerPanelH);
                    parScoreGroup.addComponent(spacerPanelH);

                    // then we rebuild the temporary containers from scratch
                    tmpPanel = new JPanel();
                    tmpPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
                    tmpLayout = new GroupLayout(tmpPanel);
                    tmpSeqScoreGroup = tmpLayout.createSequentialGroup();
                    tmpParScoreGroup = tmpLayout.createParallelGroup(CENTER);
                    i=0;
                }

                // add the student and score to the temporary panel
                // first set up a mini student panel
                JPanel studentPanel = new JPanel();
                GroupLayout studentLayout = new GroupLayout(studentPanel);
                JLabel studentName = new JLabel(student.getFullName());
                ContextField score = new ContextField(Double.toString(student.getGrade(assignment.getClassId())), student);
                //keep track of all the scores we are adding and the student associated with them
                contextFields.add(score);

                score.setMinimumSize(new Dimension(40,15));
                studentName.setMinimumSize(new Dimension(200, 15));

                studentLayout.setHorizontalGroup(studentLayout.createSequentialGroup()
                        .addComponent(studentName)
                        .addComponent(score)
                );
                studentLayout.setVerticalGroup(studentLayout.createParallelGroup(CENTER)
                        .addComponent(studentName)
                        .addComponent(score)
                );
                studentPanel.setLayout(studentLayout);

                // now add the student panel to the temporary column layout
                tmpSeqScoreGroup.addComponent(studentPanel);
                tmpParScoreGroup.addComponent(studentPanel);
                i++;
            }

            // finalize the last column
            if(i>0) {
                tmpLayout.setVerticalGroup(tmpSeqScoreGroup);
                tmpLayout.setHorizontalGroup(tmpParScoreGroup);
                tmpPanel.setLayout(tmpLayout);
                seqScoreGroup.addComponent(tmpPanel);
                parScoreGroup.addComponent(tmpPanel);
                seqScoreGroup.addComponent(spacerPanelH);
                parScoreGroup.addComponent(spacerPanelH);

                // place the columns into the score container
                scoreLayout.setHorizontalGroup(seqScoreGroup);
                scoreLayout.setVerticalGroup(parScoreGroup);
                scorePanel.setLayout(scoreLayout);
            }
        }
        // assembles everything into the parent grouping
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(headerPanel)
                .addComponent(description)
                .addComponent(scorePanel)
                .addComponent(editButton)
                .addComponent(saveButton)
                .addComponent(backButton)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(headerPanel)
                .addComponent(description)
                .addComponent(scorePanel)
                .addComponent(editButton)
                .addComponent(saveButton)
                .addComponent(backButton)
        );

        panel.setLayout(layout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }

    private void editAssignment() {
        EditAssignmentView editAssignmentView = new EditAssignmentView(this.assignment);
        editAssignmentView.setVisible(true);
        end();
    }

    private void saveAssignment(){
        /*Save the assignment*/

        float tempScore;
        for(ContextField ctx : contextFields){
            Student tempStudent = (Student) ctx.getContext();
            tempScore = (float)Double.parseDouble(ctx.getText());
            tempStudent.setScore(this.assignment, tempStudent, tempScore);
        }

        Course tempCourse = assignment.getCourse();
        CourseView courseView = new CourseView(tempCourse);
        courseView.setVisible(true);
        end();
    }

    private void back(){
        Course tempCourse = assignment.getCourse();
        CourseView courseView = new CourseView(tempCourse);
        courseView.setVisible(true);
        end();
    }
}

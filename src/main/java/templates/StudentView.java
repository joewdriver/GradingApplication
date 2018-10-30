package templates;

import models.Class;
import models.Group;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class StudentView extends JFrame {
    private ArrayList<Class> classes;
    private JLabel name;
    private JLabel graduateLevel;
    private JComboBox groups;
    private Student student;

    public StudentView(Student student) {
        this.student = student;
        this.classes = student.getClasses();
        this.createUIComponents();
        setTitle("Grading Records - " + student.getName());
        setSize(700,400);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        name = new JLabel(student.getName());
        graduateLevel = new JLabel(student.getGraduateLevel());
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel corePanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel(new GridLayout(classes.size() + 1,3));

        // top level vertical container for two sub-panels
        GroupLayout coreLayout = new GroupLayout(corePanel);

        coreLayout.setHorizontalGroup(coreLayout.createParallelGroup()
                .addComponent(topPanel)
                .addComponent(bottomPanel)
        );

        coreLayout.setVerticalGroup(coreLayout.createSequentialGroup()
                .addComponent(topPanel)
                .addComponent(bottomPanel)
        );

        corePanel.setLayout(coreLayout);

        GroupLayout topLayout = new GroupLayout(topPanel);
        topLayout.setHorizontalGroup(topLayout.createSequentialGroup()
                .addComponent(name)
                .addComponent(graduateLevel));

        topLayout.setVerticalGroup(topLayout.createParallelGroup(CENTER)
                .addComponent(name)
                .addComponent(graduateLevel));

        topPanel.setLayout(topLayout);

        coreLayout.setAutoCreateContainerGaps(true);
        topLayout.setAutoCreateContainerGaps(true);

        // three column headers, fixed in this grid, we never get wider
        bottomPanel.add("top left", new JLabel("Class"));
        bottomPanel.add("top left", new JLabel("Grade"));
        bottomPanel.add("top left", new JLabel("Group"));

        // set up an even listener for classes, and another for groups
        ActionListener alGoToClass = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                goToClass(e.getSource().toString());
            }
        };

        ActionListener alGoToGroup = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // retrieve the calling button and get its text object to pass in.
                //
                goToGroup(e.getSource().toString());
            }
        };

        // now loop the classes -- avoid the reserved term
        for(Class section:classes) {
            // add the action listener to the button
            JButton tempClass = new JButton(section.getSectionNumber() + " " + section.getName());
            tempClass.addActionListener(alGoToClass);
            bottomPanel.add(tempClass);

            // display the grade as a non-editable label
            bottomPanel.add(new Label(Integer.toString(student.getGrade(section.getSectionNumber())),JLabel.CENTER));

            // add an action listener to the group
            JButton tempGroup = new JButton(section.getGroup(student.getBuId()));
            tempGroup.addActionListener(alGoToGroup);
            bottomPanel.add(tempGroup);
        }

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(corePanel);
    }

    private void goToClass(String classId) {
        ClassView classes = new ClassView(classId);
        classes.setVisible(true);
    }

    private void goToGroup(String groupId) {
        //TODO: to be added once we add the group view
        return;
    }
}

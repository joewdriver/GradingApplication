package templates;

import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class LoginView extends JFrame {
    private JButton loginButton;
    private JLabel loginPrompt;
    private JTextField username;
    private JPasswordField password;

    public LoginView() {
        this.createUIComponents();
        setTitle("Grading Records - Login");
        setSize(700,400);
        setLocationRelativeTo(null);
        this.buildLayout();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createUIComponents() {

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClassesView classes = new ClassesView();
                classes.setVisible(true);
            }
        };
        loginButton = new JButton("Submit");
        loginButton.addActionListener(al);

        loginPrompt = new JLabel("Please Log In");
        username = new JTextField("username");
        username.setPreferredSize(new Dimension(200,10));
        password = new JPasswordField("password");
        password.setPreferredSize(new Dimension(200,10));
    }

    private void buildLayout() {
        Container pane = getContentPane();

        JPanel panel = new JPanel();

        // Group Layout helps us put things into a grid
        GroupLayout layout = new GroupLayout(panel);

        // creates gaps between edge of window and components
        layout.setAutoCreateContainerGaps(true);

        // the sequential groups are a set of elements that comprise a row or column.  By making the
        // horizontal group parallel, we are saying these should appear in the same column.  Parallel
        // Groups also handle alignment
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(loginButton)
                .addComponent(loginPrompt)
                .addComponent(username,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(password,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        // by making the vertical group sequential, we order the products top to bottom
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(loginPrompt)
                .addComponent(username,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(password,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(loginButton)
        );

        panel.setLayout(layout);

        // Group Layout doesn't really let us center align since it is relatively built, so we need to use another layout
        // that wraps it and gives us the center aligned look.
        pane.setLayout(new GridBagLayout());
        pane.add(panel);
    }
}

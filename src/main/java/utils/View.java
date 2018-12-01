package utils;

import javax.swing.*;

/**
 * Created by jdriver on 11/12/18.
 */
public abstract class View extends JFrame{

    protected DBManager db = new DBManager();

    protected void setup(int width, int height, String title) {
        setTitle(title);
        setSize(width,height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected void end() {
        db.closeDB();
        dispose();
    }
}

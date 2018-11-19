package utils;

import javax.swing.*;

/**
 * Created by jdriver on 11/12/18.
 */
public abstract class View extends JFrame{

    protected void setup(int width, int height, String title) {
        setTitle(title);
        setSize(width,height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

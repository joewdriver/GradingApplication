package utils;

import javax.swing.*;

/**
 * Created by jdriver on 10/30/18.
 * this is an extension of the Jbutton class that allows us to add a context object to be passed down
 * to a calling actionListener.  This will allows us to pass fully built objects in the code and help
 * us avoid making repeated db calls.
 */
public class ContextButton extends JButton {

    private Object context;

    public ContextButton(String name, Object context) {
        super(name);
        this.context=context;
    }

    public Object getContext() {
        return this.context;
    }

    public void setContext(Object context) {
        this.context=context;
    }
}

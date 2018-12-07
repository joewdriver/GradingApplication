package utils;

import javax.swing.*;

/**
 * Created by Armin Sabouri on 12/7/2018
 */

public class ContextField extends JTextField {

    private Object context;

    public ContextField(String name, Object context) {
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

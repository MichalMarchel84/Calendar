package controller;

import view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditWindowController implements ActionListener {

    private final DayViewController parentController;
    private final EditWindow window;
    private final Entry entry;

    public EditWindowController(DayViewController parentController, EditWindow window) {
        this.parentController = parentController;
        this.window = window;
        this.entry = window.entry;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(window.accept)){

        }

        else if(e.getSource().equals(window.cancel)){
            window.dispose();
        }
    }
}

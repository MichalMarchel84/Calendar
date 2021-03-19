package view;

import model.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

abstract class Entry extends JPanel {
    final EntryLabel label;
    private final EntryModel model;

    Entry(EntryModel model){
        this.model = model;
        label = new EntryLabel(this);
        model.addActionListener(label, false);
    }

    EntryModel getModel(){
        return model;
    }
}

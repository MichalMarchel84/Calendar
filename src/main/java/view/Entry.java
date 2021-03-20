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

    LocalDateTime getTime() {
        return model.getTime();
    }

    String getTitle(){
        return getModel().getTitle();
    }

    String getDescription(){
        return getModel().getDescription();
    }

    void setTime(LocalDateTime time){
        model.setTime(time);
    }

    void setTitle(String title){
        getModel().setTitle(title);
    }

    void setDescription(String description){
        getModel().setDescription(description);
    }

    protected EntryModel getModel(){
        return model;
    }
}

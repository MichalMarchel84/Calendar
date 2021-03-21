package view;

import model.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class Entry extends JPanel {
    final EntryLabel label;
    private final EntryModel model;

    Entry(EntryModel model){
        this.model = model;
        label = new EntryLabel(this);
        model.addActionListener(label, false);
    }

    public LocalDateTime getTime() {
        return model.getTime();
    }

    public String getTitle(){
        return getModel().getTitle();
    }

    public String getDescription(){
        return getModel().getDescription();
    }

    public void setTime(LocalDateTime time){
        model.setTime(time);
    }

    public void setTitle(String title){
        getModel().setTitle(title);
    }

    public void setDescription(String description){
        getModel().setDescription(description);
    }

    public EntryModel getModel(){
        return model;
    }
}

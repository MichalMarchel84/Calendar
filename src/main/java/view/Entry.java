package view;

import model.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

abstract class Entry extends JPanel {
    final EntryLabel label;
    private final EntryModel model;
    final boolean repetitive;

    Entry(EntryModel model){
        if(model instanceof RepetitiveModel){
            repetitive = true;
        }
        else {
            repetitive = false;
        }
        this.model = model;
        label = new EntryLabel(this);
    }

    LocalDateTime getTime(){
        return null;
    }

    void setTime(LocalDateTime t){}

    EntryModel getModel(){
        return model;
    }

    void setTitle(String title){
        getModel().setTitle(title);
        label.setTitle(title);
    }
}

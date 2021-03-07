package view;

import javax.swing.*;
import java.time.LocalDateTime;

class Entry extends JPanel {
    protected LocalDateTime time;
    final EntryLabel label;
    private String title = "";
    private String description = "";
    private Repetitive repetitive = null;

    Entry(LocalDateTime time){
        this.time = time;
        label = new EntryLabel(this);
    }

    LocalDateTime getTime() {
        return time;
    }

    void setTime(LocalDateTime time) {
        this.time = time;
        label.setTime(time);
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
        this.label.setTitle(title);
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void addRepetitive(Repetitive r){
        this.repetitive = r;
    }

    void removeRepetitive(){
        this.repetitive = null;
    }

    Repetitive getRepetitive(){
        return repetitive;
    }

    boolean isRepetitive(){
        if(repetitive != null){
            return true;
        }
        else {
            return false;
        }
    }

    boolean isFirstRepetitive(){
        boolean res = false;
        if(repetitive != null){
            if(this.time.equals(repetitive.getContent().getTime())){
                res = true;
            }
        }
        return res;
    }
}

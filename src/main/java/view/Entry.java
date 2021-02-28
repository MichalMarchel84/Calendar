package view;

import javax.swing.*;
import java.time.LocalDateTime;

class Entry extends JPanel {
    protected LocalDateTime time;
    final EntryLabel label;
    private String title = "";
    private String description = "";

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
        label.setTitle(this.title);
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }
}

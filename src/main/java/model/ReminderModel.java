package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class ReminderModel extends EntryModel {

    private LocalDateTime time;

    ReminderModel(int entryID, LocalDateTime time, String title, String description) {
        super(entryID, title, description);
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {

        this.time = time;
        for (ActionListener al : listeners){
            al.actionPerformed(new ActionEvent(this, 2, "time changed"));
        }
    }
}

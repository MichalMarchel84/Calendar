package model;

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
    }
}
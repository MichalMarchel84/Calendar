package model;

import java.time.LocalDateTime;

public class ReminderModel extends EntryModel {

    private LocalDateTime time;

    public ReminderModel(LocalDateTime time, String title, String description) {
        super(1, title, description);
        this.time = time;
    }

    public ReminderModel(LocalDateTime time) {
        this(time, "", "");
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}

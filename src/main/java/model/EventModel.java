package model;

import java.time.LocalDateTime;

public class EventModel extends EntryModel {

    private long duration;

    EventModel(int entryID, LocalDateTime time, long duration, String title, String description) {
        super(entryID, time, title, description);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        fireActionEvent(4, "Duration changed");
    }
}

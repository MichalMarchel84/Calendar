package model.models;

import java.time.LocalDateTime;

public class RepetitiveEventModel extends RepetitiveModel {

    LocalDateTime startTime;
    LocalDateTime endTime;

    public RepetitiveEventModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, LocalDateTime startTime, LocalDateTime endTime) {
        super(entryID, title, description, startAt, finishedAt, interval);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

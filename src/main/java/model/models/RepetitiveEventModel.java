package model.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepetitiveEventModel extends RepetitiveModel {

    LocalDateTime time;
    long duration;

    public RepetitiveEventModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, long duration, LocalDateTime time) {
        super(entryID, title, description, startAt, finishedAt, interval);
        this.time = time;
        this.duration = duration;
    }

    public RepetitiveEventModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, long duration){
        this(entryID, title, description, startAt, finishedAt, interval, duration, LocalDateTime.from(startAt));
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public RepetitiveEventModel copy(LocalDateTime time){
        return new RepetitiveEventModel(super.getEntryID(), super.getTitle(), super.getDescription(), super.getStartAt(), super.getFinishedAt(), super.getInterval(), duration, LocalDateTime.from(time));
    }

    @Override
    public String toString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String fin;
        if(getFinishedAt() == null){
            fin = "ongoing";
        }
        else {
            fin = getFinishedAt().format(dtf);
        }
        return "id: " + getEntryID() + " started at: " + getStartAt().format(dtf) + " finished at: " + fin + " time: " + time.format(dtf);
    }
}

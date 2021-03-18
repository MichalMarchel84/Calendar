package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepetitiveEventModel extends RepetitiveModel {

    private LocalDateTime time;
    private long duration;

    RepetitiveEventModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, long duration, LocalDateTime time) {
        super(entryID, title, description, startAt, finishedAt, interval);
        this.time = time;
        this.duration = duration;
    }

    RepetitiveEventModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, long duration){
        this(entryID, title, description, startAt, finishedAt, interval, duration, LocalDateTime.from(startAt));
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {

        this.duration = duration;
        for (ActionListener al : listeners){
            al.actionPerformed(new ActionEvent(this, 6, "duration changed"));
        }
    }

    RepetitiveEventModel copy(LocalDateTime time){
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

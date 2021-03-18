package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RepetitiveReminderModel extends RepetitiveModel {

    private LocalDateTime time;

    RepetitiveReminderModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval, LocalDateTime time) {
        super(entryID, title, description, startAt, finishedAt, interval);
        this.time = time;
    }

    RepetitiveReminderModel(int entryID, String title, String description, LocalDateTime startAt, LocalDateTime finishedAt, int interval) {
        this(entryID, title, description, startAt, finishedAt, interval, LocalDateTime.from(startAt));
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

    RepetitiveReminderModel copy(LocalDateTime time){
        return new RepetitiveReminderModel(getEntryID(), getTitle(), getDescription(), getStartAt(), getFinishedAt(), getInterval(), LocalDateTime.from(time));
    }

    public ArrayList<RepetitiveReminderModel> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<RepetitiveReminderModel> list = new ArrayList<>();
        LocalDateTime occurrenceTime = getFirstAfter(t1.minusMinutes(1)); //including result for t1
        while ((occurrenceTime != null) && occurrenceTime.isBefore(t2.plusMinutes(1))){ //including result for t2
            list.add(copy(occurrenceTime));
            occurrenceTime = getFirstAfter(occurrenceTime);
        }
        return list;
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

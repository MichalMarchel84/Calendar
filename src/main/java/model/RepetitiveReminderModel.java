package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RepetitiveReminderModel extends RepetitiveModel {

    RepetitiveReminderModel(int entryID, LocalDateTime startAt, LocalDateTime finishedAt, int interval, String title, String description) {
        super(entryID, startAt, finishedAt, interval, title, description);
    }

    public ArrayList<LocalDateTime> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<LocalDateTime> list = new ArrayList<>();
        LocalDateTime occurrenceTime = getFirstAfter(t1.minusMinutes(1)); //including result for t1
        while ((occurrenceTime != null) && occurrenceTime.isBefore(t2.plusMinutes(1))){ //including result for t2
            list.add(occurrenceTime);
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
        return "id: " + getEntryID() + " started at: " + getTime().format(dtf) + " finished at: " + fin;
    }
}

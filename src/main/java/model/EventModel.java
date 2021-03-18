package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class EventModel extends EntryModel {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    EventModel(int entryID, LocalDateTime startTime, LocalDateTime endTime, String title, String description) {
        super(entryID, title, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTime(LocalDateTime t1, LocalDateTime t2) {

        if(t1.isBefore(t2)){
            this.startTime = t1;
            this.endTime = t2;
        }
        else{
            this.startTime = t2;
            this.endTime = t1;
        }

        for (ActionListener al : listeners){
            al.actionPerformed(new ActionEvent(this, 2, "time changed"));
        }
    }
}

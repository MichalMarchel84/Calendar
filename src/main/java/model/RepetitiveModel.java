package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public abstract class RepetitiveModel extends EntryModel {

    private LocalDateTime finishedAt;
    private int interval;
    public static final String[] intervalTypes = {"month", "year", "period"};

    RepetitiveModel(int entryID, LocalDateTime time, LocalDateTime finishedAt, int interval, String title, String description) {
        super(entryID, time, title, description);
        this.finishedAt = finishedAt;
        this.interval = interval;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {

        this.finishedAt = finishedAt;
        fireActionEvent(5, "FinishedAt changed");
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {

        this.interval = interval;
        fireActionEvent(6, "Interval changed");
    }

    LocalDateTime getFirstAfter(LocalDateTime time){
        LocalDateTime res = null;
        if((finishedAt == null) || time.isBefore(finishedAt)) {
            res = LocalDateTime.from(getTime());
            while (res.isBefore(time) || res.equals(time)) {
                if (interval == -1) {
                    res = res.plusYears(1);
                } else if (interval == 0) {
                    res = res.plusMonths(1);
                } else {
                    res = res.plusDays(interval);
                }
            }
        }
        return res;
    }
}

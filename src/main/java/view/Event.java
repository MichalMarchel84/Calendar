package view;

import info.clearthought.layout.TableLayout;
import model.EventModel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class Event extends Entry {

    static final Color singleColor = new Color(0, 0, 255, 100);
    static final Color repetitiveColor = new Color(0, 255, 0, 100);

    Event(EventModel model) {
        super(model);
        label.setTime(getTimeStart(), getTimeEnd());
        if(repetitive){
            this.setBackground(singleColor);
        }
        else {
            this.setBackground(repetitiveColor);
        }
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        double[] cols = {TableLayout.FILL};
        double[] rows = {10, TableLayout.FILL, 10};
        this.setLayout(new TableLayout(cols, rows));
        JPanel center = new JPanel();
        center.setBackground(new Color(0, 0, 0, 0));
        center.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(center, "0 1 f f");
    }

    LocalDateTime getTimeStart(){
        return getModel().getStartTime();
    }

    LocalDateTime getTimeEnd(){
        return getModel().getEndTime();
    }

    void setTimeStart(LocalDateTime t){
        getModel().setTime(t, getModel().getEndTime());
        label.setTime(getTimeStart(), getTimeEnd());
    }

    void setTimeEnd(LocalDateTime t){
        getModel().setTime(getModel().getStartTime(), t);
        label.setTime(getTimeStart(), getTimeEnd());
    }

    @Override
    void setTime(LocalDateTime t){
        long diff = t.until(getModel().getStartTime(), ChronoUnit.MINUTES);
        getModel().setTime(getTimeStart().minusMinutes(diff), getTimeEnd().minusMinutes(diff));
        label.setTime(getTimeStart(), getTimeEnd());
    }

    @Override
    EventModel getModel(){
        return (EventModel) super.getModel();
    }
}

package view;

import info.clearthought.layout.TableLayout;
import model.EventModel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

class Event extends Entry {

    static final Color singleColor = new Color(0, 0, 255, 100);
    static final Color repetitiveColor = new Color(0, 255, 0, 100);

    Event(EventModel model) {
        super(model);
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

    @Override
    LocalDateTime getTime(){
        return getModel().getTime();
    }

    LocalDateTime getTimeEnd(){
        return getTime().plusMinutes(getDuration());
    }

    long getDuration(){
        return getModel().getDuration();
    }

    void setDuration(long val){
        getModel().setDuration(val);
    }

    @Override
    void setTime(LocalDateTime t){
        getModel().setTime(t);
    }

    @Override
    EventModel getModel(){
        return (EventModel) super.getModel();
    }
}

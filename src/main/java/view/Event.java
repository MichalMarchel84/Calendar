package view;

import info.clearthought.layout.TableLayout;
import model.EventModel;
import javax.swing.*;
import java.awt.*;

class Event extends Entry {

    static final Color singleColor = new Color(0, 0, 255, 100);
    static final Color repetitiveColor = new Color(0, 255, 0, 100);

    Event(EventModel model) {
        super(model);
        label.setTime(getModel().getTime(), getModel().getTimeEnd());
        label.setTitle(getModel().getTitle());
        if(getModel().isRepetitive()){
            this.setBackground(repetitiveColor);
        }
        else {
            this.setBackground(singleColor);
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
    EventModel getModel(){
        return (EventModel) super.getModel();
    }
}

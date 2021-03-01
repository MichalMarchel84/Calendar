package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

class Event extends Entry {

    private LocalDateTime timeEnd;

    Event(LocalDateTime timeStart, LocalDateTime timeEnd) {
        super(timeStart);
        this.timeEnd = timeEnd;
        label.setTime(this.time, this.timeEnd);
        this.setBackground(new Color(0, 0, 255, 100));
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

    public LocalDateTime getTimeStart() {
        return time;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.time = timeStart;
        label.setTime(this.time, this.timeEnd);
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
        label.setTime(this.time, this.timeEnd);
    }
}

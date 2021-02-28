package view;

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

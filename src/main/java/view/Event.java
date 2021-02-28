package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

class Event extends JPanel {

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    final EntryLabel label;

    Event(LocalDateTime timeStart, LocalDateTime timeEnd) {
        label = new EntryLabel(this);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        label.setTime(this.timeStart, this.timeEnd);
        this.setBackground(new Color(0, 0, 255, 100));
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
        label.setTime(this.timeStart, this.timeEnd);
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
        label.setTime(this.timeStart, this.timeEnd);
    }

}

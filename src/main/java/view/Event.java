package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

class Event extends JPanel {

    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    final EntryLabel label = new EntryLabel();

    Event(LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        label.setTime(this.timeStart, this.timeEnd);
        this.setBackground(Color.BLUE);
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
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

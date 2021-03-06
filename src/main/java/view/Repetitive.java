package view;

import java.time.LocalDateTime;

class Repetitive{

    static final String[] types = {"Month", "Year", "Period"};
    private LocalDateTime timeEnd = null;
    private final Entry content;
    private int type = 0;
    private int period = 7;

    Repetitive(Entry content) {
        this.content = content;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}

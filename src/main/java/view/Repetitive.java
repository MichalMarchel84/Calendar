package view;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class Repetitive{

    static final String[] types = {"Month", "Year", "Period"};
    private LocalDateTime timeEnd = null;
    private final Entry content;
    private int type = 0;
    private int period = 7;
    private final ArrayList<Entry> entries = new ArrayList<>();

    Repetitive(Entry content) {
        this.content = content;
    }

    void setEntriesFor(LocalDateTime t1, LocalDateTime t2){
        if(t2.isAfter(content.getTime())){
            if((timeEnd == null) || t1.isBefore(timeEnd)){
                entries.add(getFirstAfter(t1));
                while (entries.get(entries.size() - 1).getTime().isBefore(t2)){
                    entries.add(getFirstAfter(entries.get(entries.size() - 1).getTime().plusMinutes(1)));
                }
                entries.remove(entries.size() - 1);
            }
        }
    }

    ArrayList<Entry> getEntries(){
        return entries;
    }

    private Entry getFirstAfter(LocalDateTime time){
        LocalDateTime entryTime = content.getTime();
        while (entryTime.isBefore(time)){
            if(type == 0){
                entryTime = entryTime.plusMonths(1);
            }
            else if(type == 1){
                entryTime = entryTime.plusYears(1);
            }
            else if(type == 2){
                entryTime = entryTime.plusDays(period);
            }
        }
        return copyWithOffset(content, entryTime);
    }

    private Entry copyWithOffset(Entry entry, LocalDateTime time){
        Entry res = null;
        if(entry instanceof Reminder){
            res = new Reminder(time);
        }
        else if(entry instanceof Event){
            Event e = (Event) entry;
            long p = e.getTimeStart().until(e.getTimeEnd(), ChronoUnit.MINUTES);
            res = new Event(time, time.plusMinutes(p));
        }
        res.setTitle(content.getTitle());
        res.setDescription(content.getDescription());
        res.addRepetitive(this);
        return res;
    }

    void setTitle(String title){
        content.setTitle(title);
        for(Entry e : entries){
            e.setTitle(title);
        }
    }

    void setDescription(String description){
        content.setDescription(description);
        for(Entry e : entries){
            e.setDescription(description);
        }
    }

    LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    int getType() {
        return type;
    }

    void setType(int type) {
        this.type = type;
    }

    int getPeriod() {
        return period;
    }

    void setPeriod(int period) {
        this.period = period;
    }

    Entry getContent(){
        return content;
    }
}

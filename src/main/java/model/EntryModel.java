package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class EntryModel {

    private boolean repetitive = false;
    private final int entryID;
    private String title;
    private String description;
    private LocalDateTime time;
    final ArrayList<ActionListener> transactionListeners = new ArrayList<>();
    final ArrayList<ActionListener> listeners = new ArrayList<>();
    private boolean suspendEvents = false;

    EntryModel(int entryID, LocalDateTime time, String title, String description) {
        this.entryID = entryID;
        this.time = time;
        this.title = title;
        this.description = description;
    }

    public boolean isRepetitive(){
        return repetitive;
    }

    void setRepetitive(){
        repetitive = true;
    }

    public int getEntryID() {
        return entryID;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time){

        this.time = time;
        fireActionEvent(1, "Time changed");
    }

    public void setTitle(String title) {

        this.title = title;
        fireActionEvent(2, "Title changed");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
        fireActionEvent(3, "Description changed");
    }

    public void addActionListener(ActionListener actionListener, boolean transactionsEnabled){
        if(transactionsEnabled){
            transactionListeners.add(actionListener);
        }
        else {
            listeners.add(actionListener);
        }
    }

    public void beginTransaction(){
        suspendEvents = true;
    }

    public void commit(){
        suspendEvents = false;
        fireActionEvent(0, "changes committed");
    }

    protected void fireActionEvent(int id, String text){

        ActionEvent event = new ActionEvent(this, id, text);
        if(!suspendEvents) {

            for (ActionListener al : transactionListeners) {
                al.actionPerformed(event);
            }
        }

        if(id != 0) {
            for (ActionListener al : listeners) {
                al.actionPerformed(event);
            }
        }
    }
}

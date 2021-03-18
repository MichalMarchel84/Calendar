package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class EntryModel {

    private final int entryID;
    private String title;
    private String description;
    final ArrayList<ActionListener> listeners = new ArrayList<>();

    EntryModel(int entryID, String title, String description) {
        this.entryID = entryID;
        this.title = title;
        this.description = description;
    }

    public int getEntryID() {
        return entryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
        for (ActionListener al : listeners){
            al.actionPerformed(new ActionEvent(this, 0, "title changed"));
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
        for (ActionListener al : listeners){
            al.actionPerformed(new ActionEvent(this, 1, "description changed"));
        }
    }

    public void addActionListener(ActionListener actionListener){
        listeners.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener){
        listeners.remove(actionListener);
    }
}

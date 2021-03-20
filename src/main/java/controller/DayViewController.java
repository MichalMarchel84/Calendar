package controller;

import model.*;
import view.DayViewPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DayViewController implements ActionListener {

    private Client client = null;
    public final DayViewPanel dayView;
    private final MonthViewController parent;

    public DayViewController(MonthViewController parent) {
        this.parent = parent;
        dayView = new DayViewPanel(this);
    }

    public void setClient(Client client){
        clearContent();
        this.client = client;
    }

    public void setDate(LocalDate date){
        dayView.setDate(date);
    }

    public ReminderModel createReminderModel(LocalDateTime time){
        ReminderModel model = client.createReminder(time, "", "");
        model.addActionListener(this, true);
        return model;
    }

    public EventModel createEventModel(LocalDateTime time, long duration){
        EventModel model = client.createEvent(time, duration, "", "");
        model.addActionListener(this, true);
        return model;
    }

    public RepetitiveReminderModel createRepetitiveReminderModel(ReminderModel in, int interval){
        RepetitiveReminderModel model = client.createRepetitiveReminder(in.getTime(), interval, in.getTitle(), in.getDescription());
        model.addActionListener(this, true);
        return model;
    }

    public RepetitiveEventModel createRepetitiveEventModel(EventModel in, int interval){
        RepetitiveEventModel model = client.createRepetitiveEvent(in.getTime(), in.getDuration(), interval, in.getTitle(), in.getDescription());
        model.addActionListener(this, true);
        return model;
    }

    public ArrayList<ReminderModel> getRemindersBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<ReminderModel> list = client.getRemindersBetween(t1, t2);
        for (ReminderModel model : list){
            model.addActionListener(this, true);
        }
        return list;
    }

    public ArrayList<EventModel> getEventsBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<EventModel> list = client.getEventsBetween(t1, t2);
        for (EventModel model : list){
            model.addActionListener(this, true);
        }
        return list;
    }

    public ArrayList<RepetitiveReminderModel> getRepetitiveRemindersBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<RepetitiveReminderModel> list = client.getRepetitiveRemindersBetween(t1, t2);
        for(RepetitiveReminderModel model : list){
            model.addActionListener(this, true);
        }
        return list;
    }

    public ArrayList<RepetitiveEventModel> getRepetitiveEventsBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<RepetitiveEventModel> list = client.getRepetitiveEventsBetween(t1, t2);
        for(RepetitiveEventModel model : list){
            model.addActionListener(this, true);
        }
        return list;
    }

    public void delete(EntryModel model){
        client.delete(model);
    }

    public void clearContent(){

        client = null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        EntryModel model = (EntryModel) actionEvent.getSource();
        client.update(model);
    }
}

package controller;

import model.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Controller implements ActionListener {

    private Client client = null;

    public Controller() {
    }

    public void login(String login, String pass, boolean newClient){
        try {
            client = new Client(login, pass, newClient);
            App.appWindow.login();
        }
        catch (LoginPanelException e){
            App.appWindow.displayError(e.getMessage());
        }
    }

    public void logout(){
        client = null;
    }

    /*public Client getClient(){
        return client;
    }*/

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

    public void delete(EntryModel model){
        client.delete(model);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        EntryModel model = (EntryModel) actionEvent.getSource();
        client.update(model);
    }
}

package controller;

import model.EntryModel;
import view.*;
import view.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class EditWindowController implements ActionListener {

    private final DayViewController parentController;
    private final EditWindow window;
    private final Entry entry;
    private final JTextField from;
    private final JTextField to;
    private final JTextField title;
    private final JTextArea description;
    private final JCheckBox repetitive;
    private final JComboBox<String> type;
    private final JTextField period;

    private LocalDateTime fromTime;
    private long duration;
    private int periodInt;
    private LocalDateTime repetitiveClosed;

    public EditWindowController(DayViewController parentController, EditWindow window) {
        this.parentController = parentController;
        this.window = window;
        this.entry = window.entry;
        from = window.from;
        to = window.to;
        title = window.title;
        description = window.description;
        repetitive = window.repetitive;
        type = window.type;
        period = window.period;
        repetitiveClosed = window.repetitiveClosed;
        if(notFirstOccurrence()) lockRepetitiveOptions();
    }

    void pickScenarioAndExecute(){
        if(entry instanceof RepetitiveEntry){
            if(repetitive.isSelected()){
                updateEntry(true);
            }
            else {
                convertSingle();
            }
        }
        else {
            if(repetitive.isSelected()){
                convertRepetitive();
            }
            else {
                updateEntry(true);
            }
        }
    }

    void updateEntry(boolean sendChanges){
        if(entry instanceof Reminder){
            updateReminder(sendChanges);
        }
        else if(entry instanceof Event){
            updateEvent(sendChanges);
        }
        else if(entry instanceof RepetitiveReminder){
            updateRepetitiveReminder();
        }
        else if(entry instanceof RepetitiveEvent){
            updateRepetitiveEvent();
        }
    }

    void updateReminder(boolean sendChanges){
        Reminder reminder = (Reminder) entry;
        reminder.getModel().beginTransaction();
        reminder.setTime(fromTime);
        reminder.setTitle(title.getText());
        reminder.setDescription(description.getText());
        reminder.getModel().commit();
        if(sendChanges) {
            parentController.updateReminder(reminder);
        }
    }

    void updateEvent(boolean sendChanges){
        Event event = (Event) entry;
        event.getModel().beginTransaction();
        event.setTime(fromTime);
        event.setDuration(duration);
        event.setTitle(title.getText());
        event.setDescription(description.getText());
        event.getModel().commit();
        if(sendChanges) {
            parentController.updateEvent(event);
        }
    }

    void updateRepetitiveReminder(){
        RepetitiveReminder reminder = (RepetitiveReminder) entry;
        reminder.getModel().beginTransaction();
        reminder.setStartAt(reminder.getStartAt().with(fromTime.toLocalTime()));
        reminder.setFinishedAt(repetitiveClosed);
        reminder.setTitle(title.getText());
        reminder.setDescription(description.getText());
        reminder.getModel().setInterval(periodInt);
        reminder.getModel().commit();
        parentController.updateRepetitive(reminder);
    }

    void updateRepetitiveEvent(){
        RepetitiveEvent event = (RepetitiveEvent) entry;
        event.getModel().beginTransaction();
        event.setStartAt(event.getStartAt().with(fromTime.toLocalTime()));
        event.setFinishedAt(repetitiveClosed);
        event.setDuration(duration);
        event.setTitle(title.getText());
        event.setDescription(description.getText());
        event.getModel().setInterval(periodInt);
        event.getModel().commit();
        parentController.updateRepetitive(event);
    }

    void convertSingle(){
        EntryModel model = null;
        if(entry instanceof RepetitiveReminder){
            model = parentController.createReminderModel(entry.getTime().with(fromTime.toLocalTime()));
        }
        else if(entry instanceof RepetitiveEvent){
            model = parentController.createEventModel(entry.getTime().with(fromTime.toLocalTime()), duration);
        }
        model.beginTransaction();
        model.setTitle(entry.getTitle());
        model.setDescription(entry.getDescription());
        model.commit();
        parentController.convertToSingle((RepetitiveEntry) entry, model);
    }

    void convertRepetitive(){
        updateEntry(false);
        parentController.convertToRepetitive(entry, periodInt);
    }

    void prepareData() throws DateTimeParseException, NumberFormatException {
        fromTime = entry.getTime().with(LocalTime.parse(from.getText()));
        repetitiveClosed = window.repetitiveClosed;
        if(entry instanceof EventPattern){
            LocalDateTime endtime;
            LocalTime t = LocalTime.parse(to.getText());
            if(t.isBefore(fromTime.toLocalTime())){
                endtime = fromTime.plusDays(1).with(t);
            }
            else endtime = fromTime.with(t);
            duration = fromTime.until(endtime, ChronoUnit.MINUTES);
        }
        if(repetitive.isSelected()){
            if(type.getSelectedIndex() == 0){
                periodInt = 0;
            }
            else if(type.getSelectedIndex() == 1){
                periodInt = -1;
            }
            else if(type.getSelectedIndex() == 2) {
                periodInt = Integer.parseInt(period.getText());
            }
        }
    }

    private boolean notFirstOccurrence(){
        if(entry instanceof RepetitiveEntry){
            RepetitiveEntry rep = (RepetitiveEntry) entry;
            if(rep.getTime().equals(rep.getStartAt())){
                return false;
            }
            else {
                return true;
            }
        }
        else return false;
    }

    private void lockRepetitiveOptions(){
        from.setEnabled(false);
        to.setEnabled(false);
        type.setEnabled(false);
        period.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(window.accept)){
            try {
                prepareData();
                pickScenarioAndExecute();
                window.dispose();
            }
            catch (DateTimeParseException pe){
                from.setForeground(Color.RED);
                to.setForeground(Color.RED);
                period.setForeground(Color.BLACK);
            }
            catch (NumberFormatException ne){
                from.setForeground(Color.BLACK);
                to.setForeground(Color.BLACK);
                period.setForeground(Color.RED);
            }
        }

        else if(e.getSource().equals(window.cancel)){
            window.dispose();
        }
    }
}

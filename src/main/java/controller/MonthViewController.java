package controller;

import model.Client;
import view.MonthViewPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MonthViewController implements ActionListener {

    private final AppWindowController parent;
    private final DayViewController dayViewController;
    private final MonthViewPanel monthView;

    public MonthViewController(AppWindowController parent) {

        this.parent = parent;
        monthView = new MonthViewPanel(this);
        dayViewController = new DayViewController(this);
    }

    public void setClient(Client client){
        dayViewController.setClient(client);
    }

    public void display(){
        parent.setSize(new Dimension(1000, 800));
        monthView.inhibit = true;
        monthView.setContent();
        parent.displayPanel(monthView);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
                monthView.centerContent();
                monthView.inhibit = false;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        t.start();
    }

    public void displayDayView(LocalDate date){
        dayViewController.setDate(date);
        parent.displayPanel(dayViewController.dayView);
        parent.addReturnButton(this);
    }

    public void clearContent(){
        dayViewController.clearContent();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(parent.window.returnButton)){
            parent.displayPanel(monthView);
            parent.removeReturnButton();
        }
    }
}

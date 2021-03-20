package controller;

import model.Client;
import view.AppWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppWindowController implements ActionListener {

    public final AppWindow window = new AppWindow();
    private final LoginController loginController;
    private final MonthViewController monthViewController;

    private JPanel onDisplay;

    public AppWindowController() {
        loginController = new LoginController(this);
        monthViewController = new MonthViewController(this);
        window.logout.addActionListener(this);
        loginController.display();
    }

    public void displayPanel(JPanel panel) {
        if (onDisplay != null) {
            window.remove(onDisplay);
        }
        onDisplay = panel;
        window.add(onDisplay, "0 1 f f");
        window.revalidate();
        window.repaint();
    }

    public void setSize(Dimension size){
        window.setSize(size);
        window.setLocationRelativeTo(null);
    }

    void clearContent(){
        monthViewController.clearContent();
    }

    public void addReturnButton(ActionListener listener){
        window.returnButton.addActionListener(listener);
        window.addReturnButton();
    }

    public void removeReturnButton(){
        for(ActionListener al : window.returnButton.getActionListeners()){
            window.returnButton.removeActionListener(al);
        }
        window.topMenu.remove(window.returnButton);
    }

    public void login(Client client){
        window.addLogoutButton();
        monthViewController.setClient(client);
        monthViewController.display();
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource().equals(window.logout)){
            loginController.display();
            window.topMenu.remove(window.logout);
            window.topMenu.remove(window.returnButton);
            clearContent();
        }
    }


}

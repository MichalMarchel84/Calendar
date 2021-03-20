package controller;

import model.App;
import model.Client;
import model.LoginException;
import view.LoginPanel;
import view.NewUserPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private final AppWindowController parent;
    private final LoginPanel login = new LoginPanel(this);
    private final NewUserPanel newUser = new NewUserPanel(this);

    public LoginController(AppWindowController parent) {
        this.parent = parent;
    }

    public void display(){
        parent.displayPanel(login);
        parent.setSize(new Dimension(350, 500));
    }

    private void login(String login, String pass, boolean newClient) throws LoginException {
        Client client = new Client(login, pass, newClient);
        parent.login(client);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(login.signIn)){
            try{
                login(login.userName.getText(), String.copyValueOf(login.pass.getPassword()), false);
            }
            catch (LoginException ex){
                login.setErrorMessage(ex.getMessage());
            }
        }
        else if(e.getSource().equals(login.newUser)){
            parent.displayPanel(newUser);
        }
        else if(e.getSource().equals(newUser.create)){
            if(newUser.pass.getPassword().equals(newUser.repeatPass.getPassword())){
                try{
                    login(newUser.userName.getText(), String.copyValueOf(newUser.pass.getPassword()), true);
                }
                catch (LoginException ex){
                    newUser.setErrorMessage(ex.getMessage());
                }
            }
            else {
                newUser.setErrorMessage("error_on_pass_set");
            }
        }
        else if(e.getSource().equals(newUser.cancel)){
            parent.displayPanel(login);
        }
    }
}

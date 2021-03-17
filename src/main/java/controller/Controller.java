package controller;

import model.App;
import model.Client;
import model.LoginPanelException;

public class Controller {

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

    public Client getClient(){
        return client;
    }
}

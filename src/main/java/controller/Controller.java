package controller;

import model.LoginPanelException;
import model.Model;
import org.mindrot.jbcrypt.BCrypt;
import view.AppWindow;

public class Controller implements RequestListener{

    Model model;
    AppWindow window;

    public Controller(AppWindow window) {
        this.window = window;
    }

    @Override
    public void requestReceived(Request req) {

        if(req instanceof LoginRequest){
            LoginRequest request = (LoginRequest)req;
            try {
                String passwordHashed = Model.getPasswordHash(request.login);
                if(!verifyHash(new String(request.pass), passwordHashed)){
                    window.displayError("error_wrong_password");
                }
                else{
                    model = new Model(request.login);
                    window.login();
                }
            }
            catch (LoginPanelException e){
                window.displayError(e.getMessage());
            }
        }


        else if(req instanceof NewUserRequest){
            NewUserRequest request = (NewUserRequest) req;
            String passwordHash = hash(new String(request.pass));
            try {
                model = new Model(request.login, passwordHash);
                window.displayPanel(AppWindow.panels.monthView);
            }
            catch (LoginPanelException e) {
                window.displayError(e.getMessage());
            }
        }


        else{
            System.out.println("Unknown request");
        }
    }
    private String hash(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt(10));
    }

    private boolean verifyHash(String pass, String hash){
        return BCrypt.checkpw(pass, hash);
    }
}

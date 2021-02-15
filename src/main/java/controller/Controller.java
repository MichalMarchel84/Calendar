package controller;

import model.LoginPanelException;
import model.Model;
import org.mindrot.jbcrypt.BCrypt;
import view.LoginPanel;
import view.NewUserPanel;

public class Controller implements RequestListener{

    public Controller() {
    }

    @Override
    public void requestReceived(Request req) {

        if(req instanceof LoginRequest){
            LoginRequest request = (LoginRequest)req;
            try {
                String passwordHashed = Model.getPasswordHash(request.login);
                if(!verifyHash(new String(request.pass), passwordHashed)){
                    if (request.source instanceof LoginPanel) {
                        LoginPanel p = (LoginPanel) request.source;
                        p.setErrorMessage("error_wrong_password");
                    }
                }
                else{
                    System.out.println("All correct");
                }
            }
            catch (LoginPanelException e){
                if(request.source instanceof LoginPanel){
                    LoginPanel p = (LoginPanel)request.source;
                    p.setErrorMessage(e.getMessage());
                }
            }
        }


        else if(req instanceof NewUserRequest){
            NewUserRequest request = (NewUserRequest) req;
            String passwordHash = hash(new String(request.pass));
            try {
                Model model = new Model(request.login, passwordHash, true);
                System.out.println("Id: " + model.clientId);
            }
            catch (LoginPanelException e) {
                if(request.source instanceof NewUserPanel){
                    NewUserPanel p = (NewUserPanel) request.source;
                    p.setErrorMessage(e.getMessage());
                }
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

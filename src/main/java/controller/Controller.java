package controller;

import model.Model;
import org.mindrot.jbcrypt.BCrypt;
import view.LoginPanel;

import java.sql.SQLException;

public class Controller implements RequestListener{

    public Controller() {
    }

    @Override
    public void requestReceived(Request req) {
        if(req instanceof LoginRequest){
            LoginRequest request = (LoginRequest)req;
            String passwordHashed = Model.getPasswordHash(request.login);
            if(passwordHashed.equals("")) {
                if (request.source instanceof LoginPanel) {
                    LoginPanel p = (LoginPanel) request.source;
                    p.setErrorMessage("error_wrong_login");
                }
            }
            else if(!verifyHash(new String(request.pass), passwordHashed)){
                if (request.source instanceof LoginPanel) {
                    LoginPanel p = (LoginPanel) request.source;
                    p.setErrorMessage("error_wrong_password");
                }
            }
            else{
                System.out.println("All correct");
            }
        }
        else if(req instanceof NewUserRequest){
            NewUserRequest request = (NewUserRequest) req;
            String passwordHash = hash(new String(request.pass));
            try {
                Model model = new Model(request.login, passwordHash, true);
                System.out.println("Id: " + model.clientId);
            }
            catch (SQLException e) {
                if(request.source instanceof LoginPanel){
                    LoginPanel p = (LoginPanel)request.source;
                    if(e.getErrorCode() == 19){
                        p.setErrorMessage("error_login_in_use");
                    }
                    else{
                        p.setErrorMessage("error_unknown");
                        e.printStackTrace();
                    }
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

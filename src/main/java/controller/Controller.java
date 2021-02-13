package controller;

import org.mindrot.jbcrypt.BCrypt;
import view.LoginPanel;

public class Controller implements RequestListener{

    public Controller() {
    }

    @Override
    public void requestReceived(Request req) {
        if(req instanceof LoginRequest){
            LoginRequest request = (LoginRequest)req;
            System.out.print("Login: ");
            System.out.println(request.login);
            System.out.print("pass: ");
            char[] chars = request.pass;
            for(char c : chars){
                System.out.print(c);
            }
            if(request.source instanceof LoginPanel){
                LoginPanel p = (LoginPanel)request.source;
                p.setErrorMessage("error_not_implemented");
            }
        }
        else if(req instanceof NewUserRequest){
            NewUserRequest request = (NewUserRequest) req;
            String passwordHash = hash(new String(request.pass));
            System.out.println("Login: " + request.login);
            System.out.println("Hash: " + passwordHash);
            if(request.source instanceof LoginPanel){
                LoginPanel p = (LoginPanel)request.source;
                p.setErrorMessage("error_not_implemented");
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

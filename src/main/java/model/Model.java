package model;

public class Model {

    public int clientId;

    public Model(String login, String pass){

    }

    public Model(String login, String pass, boolean newUser) throws LoginPanelException {
        if(newUser){
            clientId = Dao.addUser(login, pass);
        }
    }

    public static void connectToDB(){
        Dao.connect();
    }

    public static void disconnectFromDB(){
        Dao.disconnect();
    }

    public static String getPasswordHash(String login) throws LoginPanelException {
        return Dao.getPasswordHash(login);
    }
}

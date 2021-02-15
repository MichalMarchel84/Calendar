package model;

public class Model {

    public int clientId;

    public Model(String login){
        clientId = Dao.getUserId(login);
    }

    public Model(String login, String pass) throws LoginPanelException {
        clientId = Dao.addUser(login, pass);
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

package model;

import java.sql.SQLException;

public class Model {

    public int clientId;

    public Model(String login, String pass){

    }

    public Model(String login, String pass, boolean newUser) throws SQLException {
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

    public static String getPasswordHash(String login){
        return Dao.getPasswordHash(login);
    }
}

package model;

public class Model {

    private final int clientId;

    public Model(String login){

        clientId = ClientDaoObs.getUserId(login);
    }

    public Model(String login, String pass) throws LoginPanelException {

        clientId = ClientDaoObs.addUser(login, pass);
    }

    public static String getPasswordHash(String login) throws LoginPanelException {
        return ClientDaoObs.getPasswordHash(login);
    }
}

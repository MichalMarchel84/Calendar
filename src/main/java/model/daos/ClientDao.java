package model.daos;

import model.App;
import model.LoginPanelException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDao {

    private final Connection conn;
    private final int clientID;

    public ClientDao(String login, String pass, boolean newClient, Connection conn) throws LoginPanelException {

        this.conn = conn;

        if(newClient){
            clientID = addUser(login, pass);
        }
        else {
            clientID = login(login, pass);
        }
    }

    public ClientDao(String login, String pass, boolean newClient) throws LoginPanelException{
        this(login, pass, newClient, App.conn);
    }

    private String hash(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt(10));
    }

    private boolean verifyHash(String login, String pass) throws LoginPanelException{
        return BCrypt.checkpw(pass, getPasswordHash(login));
    }

    private int addUser(String login, String pass) throws LoginPanelException {
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            s.setString(2, hash(pass));
            s.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new LoginPanelException("error_login_in_use");
            } else {
                e.printStackTrace();
                throw new LoginPanelException("error_unknown");
            }
        }
        return getClientIdForLogin(login);
    }

    private int login(String login, String pass) throws LoginPanelException{
        int id = -1;
        if(verifyHash(login, pass)){
            id = getClientIdForLogin(login);
        }
        else {
            throw new LoginPanelException("error_wrong_password");
        }
        return id;
    }

    private int getClientIdForLogin(String login){
        int id = -1;
        String sql = "SELECT client_id FROM clients WHERE login=?";
        try{
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            ResultSet res = s.executeQuery();
            if(res.next()){
                id = res.getInt("client_id");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    private String getPasswordHash(String login) throws LoginPanelException {
        String sql = "SELECT password FROM clients WHERE login = ?";
        String hash = "";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            ResultSet res = s.executeQuery();
            if(res.next()){
                hash = res.getString("password");
            }
            else{
                throw new LoginPanelException("error_wrong_login");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new LoginPanelException("error_unknown");
        }
        return hash;
    }

    public int getID() {
        return clientID;
    }
}

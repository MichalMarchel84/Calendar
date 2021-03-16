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

    public ClientDao(Connection conn, String login, String passHash) throws LoginPanelException {

        this.conn = conn;
        clientID = create(login, passHash);
    }

    public ClientDao(Connection conn, String login) throws LoginPanelException{

        this.conn = conn;
        clientID = getClientIdForLogin(login);
    }

    private int create(String login, String passHash) throws LoginPanelException {
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            s.setString(2, passHash);
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

    public String getPasswordHash(String login) throws LoginPanelException {
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

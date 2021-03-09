package model;

import java.sql.*;

class ClientDaoObs {

    private ClientDaoObs(){}

    static int addUser(String login, String pass) throws LoginPanelException {
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        try {
            PreparedStatement s = App.conn.prepareStatement(sql);
            s.setString(1, login);
            s.setString(2, pass);
            s.executeUpdate();
        }
        catch (SQLException e){
            if(e.getErrorCode() == 19){
                throw new LoginPanelException("error_login_in_use");
            }
            else{
                e.printStackTrace();
                throw new LoginPanelException("error_unknown");
            }
        }
        return ClientDaoObs.getUserId(login);
    }

    static String getPasswordHash(String login) throws LoginPanelException {
        String sql = "SELECT password FROM clients WHERE login = ?";
        String hash = "";
        try {
            PreparedStatement s = App.conn.prepareStatement(sql);
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

    static int getUserId(String login){
        int id = -1;
        String sql = "SELECT client_id FROM clients WHERE login=?";
        try{
            PreparedStatement s = App.conn.prepareStatement(sql);
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
}

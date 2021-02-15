package model;

import java.sql.*;

class Dao {

    private static Connection conn;

    private Dao(){}

    static void connect(){
        String url = "jdbc:sqlite:src/main/resources/calendar";
        try{
            if(conn != null){
                conn.close();
            }
            conn =  DriverManager.getConnection(url);
            System.out.println("Connected to database");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    static void disconnect(){
        if(conn != null){
            try {
                conn.close();
                System.out.println("Disconnected from database");
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    static int addUser(String login, String pass) throws LoginPanelException {
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
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
        sql = "SELECT client_id FROM clients WHERE login = ?";
        int id = -1;
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            ResultSet res = s.executeQuery();
            res.next();
            id = res.getInt("client_id");
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new LoginPanelException("error_unknown");
        }
        return id;
    }

    static String getPasswordHash(String login) throws LoginPanelException {
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
}

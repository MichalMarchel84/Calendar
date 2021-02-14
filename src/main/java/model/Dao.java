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

    static int addUser(String login, String pass) throws SQLException{
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        PreparedStatement s = conn.prepareStatement(sql);
        s.setString(1, login);
        s.setString(2, pass);
        s.executeUpdate();
        sql = "SELECT client_id FROM clients WHERE login = ?";
        s = conn.prepareStatement(sql);
        s.setString(1, login);
        ResultSet res = s.executeQuery();
        res.next();
        int id = res.getInt("client_id");
        return id;
    }

    static String getPasswordHash(String login){
        String sql = "SELECT password FROM clients WHERE login = ?";
        String hash = "";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            ResultSet res = s.executeQuery();
            if(res.next()){
                hash = res.getString("password");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return hash;
    }
}

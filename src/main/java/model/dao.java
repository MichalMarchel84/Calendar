package model;

import java.sql.*;

public class dao {

    private static Connection conn;

    private dao(){}

    public static void connect(){
        String url = "jdbc:sqlite:src/main/resources/calendar";
        try{
            if(conn != null){
                conn.close();
            }
            conn =  DriverManager.getConnection(url);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        if(conn != null){
            try {
                conn.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static boolean isLoginFree(String login){
        String sql = "SELECT client_id FROM clients WHERE login=?";
        boolean result = false;
        try{
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            ResultSet res = s.executeQuery();
            if(!res.next()){
                result = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static int addUser(String login, String pass){
        int status = 0;
        String sql = "INSERT INTO clients(login, password) VALUES(?, ?)";
        try{
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, login);
            s.setString(2, pass);
            s.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            status = 1;
        }
        return status;
    }
}

package model;


import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class TestMethods {

    public static Connection getConnection(){
        Connection connection = null;
        String url = "jdbc:sqlite:src/test/resources/calendar.db";
        try{
            connection =  DriverManager.getConnection(url);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public static void clearDB(Connection conn){
        executeScript("src/test/resources/clearDB.sql", conn);
    }

    public static void executeScript(String URL, Connection conn){
        try{
            FileReader in = new FileReader(URL);
            Scanner scan = new Scanner(in);
            scan.useDelimiter(";");
            Object[] commands = scan.tokens().toArray();
            for (Object command : commands){
                PreparedStatement stmt = conn.prepareStatement((String) command);
                stmt.executeUpdate();
            }
            in.close();
            scan.close();
        } catch (IOException | SQLException e){
            e.printStackTrace();
        }
    }

    public static void showClients(Connection conn){
        String sql = "SELECT * FROM clients";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
            while (res.next()){
                System.out.println(res.getString("client_id") + " " + res.getString("login"));
            }
            res.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}

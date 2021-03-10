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

    public static void setTestContent(Connection conn){
        clearDB(conn);

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
}

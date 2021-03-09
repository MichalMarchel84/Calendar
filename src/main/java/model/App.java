package model;

import controller.Controller;
import view.AppWindow;
import view.Comm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public static final Connection conn = connect();

    private static final Thread onExit = new Thread(App::disconnect);

    public static void main(String[] args){
        Controller controller = new Controller(new AppWindow());
        Comm.addRequestListener(controller);
        Runtime.getRuntime().addShutdownHook(onExit);
    }

    private static Connection connect(){
        Connection connection = null;
        String url = "jdbc:sqlite:src/main/resources/calendar.db";
        try{
            connection =  DriverManager.getConnection(url);
            System.out.println("Connected to database");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private static void disconnect(){
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
}

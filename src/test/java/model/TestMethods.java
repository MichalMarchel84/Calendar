package model;


import model.daos.RepetitiveDao;
import model.daos.RepetitiveEventDao;
import model.daos.RepetitiveReminderDao;
import model.models.RepetitiveEventModel;
import model.models.RepetitiveModel;
import model.models.RepetitiveReminderModel;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
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

    public static void setRepetitiveReminderContent(Connection conn, LocalDateTime startedAt, LocalDateTime finishedAt, int interval){
        RepetitiveReminderDao dao = new RepetitiveReminderDao(1, conn);
        dao.create(new RepetitiveReminderModel(dao.getNextID(), "", "", startedAt, finishedAt, interval));
    }

    public static void setRepetitiveEventContent(Connection conn, LocalDateTime startedAt, long duration, int interval){
        RepetitiveEventDao dao = new RepetitiveEventDao(1, conn);
        dao.create(new RepetitiveEventModel(dao.getNextID(), "", "", startedAt, null, interval, duration));
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

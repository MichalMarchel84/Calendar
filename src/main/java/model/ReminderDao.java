package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.TimeZone;

public class ReminderDao {

    private final int clientID;
    private final Connection conn;
    private int lastID;

    public ReminderDao(int clientID, Connection conn) {
        this.conn = conn;
        this.clientID = clientID;
        lastID = getLastID();
    }

    public ReminderDao(int clientID){
        this(clientID, App.conn);
    }

    public int getLastID(){
        int id = 0;
        String sql ="SELECT MAX(entry_id) AS id FROM reminders WHERE client_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clientID);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                id = res.getInt("id");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    int getNextID(){
        lastID++;
        return lastID;
    }

    void insert(ReminderModel reminder){
        String sql = "INSERT INTO reminders VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clientID);
            stmt.setInt(2, reminder.getEntryID());
            stmt.setInt(3, (int)reminder.getTime().toEpochSecond(ZoneOffset.UTC));
            stmt.setString(4, reminder.getTitle());
            stmt.setString(5, reminder.getDescription());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    ArrayList<ReminderModel> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<ReminderModel> list = new ArrayList<>();
        String sql = "SELECT entry_id, time, title, description FROM reminders WHERE client_id = ? AND time BETWEEN ? AND ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, clientID);
            s.setInt(2, (int)t1.toEpochSecond(ZoneOffset.UTC));
            s.setInt(3, (int)t2.toEpochSecond(ZoneOffset.UTC));
            ResultSet set = s.executeQuery();
            while (set.next()){
                LocalDateTime t = LocalDateTime.ofInstant(Instant.ofEpochSecond(set.getInt("time")), TimeZone.getTimeZone("UTC").toZoneId());
                list.add(new ReminderModel(set.getInt("entry_id"), t, set.getString("title"), set.getString("description")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}

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

public class ReminderDao extends Dao{

    public ReminderDao(int clientID, Connection conn) {
        super("reminders", clientID, conn);
    }

    public ReminderDao(int clientID){
        this(clientID, App.conn);
    }

    void create(ReminderModel r){
        String sql = "INSERT INTO reminders VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = super.getConn().prepareStatement(sql);
            stmt.setInt(1, super.getClientID());
            stmt.setInt(2, r.getEntryID());
            stmt.setInt(3, (int)r.getTime().toEpochSecond(ZoneOffset.UTC));
            stmt.setString(4, r.getTitle());
            stmt.setString(5, r.getDescription());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    void update(ReminderModel r){
        String sql = "UPDATE reminders SET time = ?, title = ?, description = ? WHERE client_id = ? AND entry_id = ?";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, (int)r.getTime().toEpochSecond(ZoneOffset.UTC));
            s.setString(2, r.getTitle());
            s.setString(3, r.getDescription());
            s.setInt(4, super.getClientID());
            s.setInt(5, r.getEntryID());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    ArrayList<ReminderModel> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<ReminderModel> list = new ArrayList<>();
        String sql = "SELECT entry_id, time, title, description FROM reminders WHERE client_id = ? AND time BETWEEN ? AND ?";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, super.getClientID());
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

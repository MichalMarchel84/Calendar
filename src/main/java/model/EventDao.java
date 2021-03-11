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

public class EventDao extends Dao{

    public EventDao(int clientID, Connection conn) {
        super("events", clientID, conn);
    }

    public EventDao(int clientID){
        this(clientID, App.conn);
    }

    void create(EventModel event){
        String sql = "INSERT INTO events VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = super.getConn().prepareStatement(sql);
            stmt.setInt(1, super.getClientID());
            stmt.setInt(2, event.getEntryID());
            stmt.setInt(3, (int)event.getStartTime().toEpochSecond(ZoneOffset.UTC));
            stmt.setInt(4, (int)event.getEndTime().toEpochSecond(ZoneOffset.UTC));
            stmt.setString(5, event.getTitle());
            stmt.setString(6, event.getDescription());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    void update(EventModel event){
        String sql = "UPDATE events SET start_time = ?, end_time = ?, title = ?, description = ? WHERE client_id = ? AND entry_id = ?";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, (int)event.getStartTime().toEpochSecond(ZoneOffset.UTC));
            s.setInt(2, (int)event.getEndTime().toEpochSecond(ZoneOffset.UTC));
            s.setString(3, event.getTitle());
            s.setString(4, event.getDescription());
            s.setInt(5, super.getClientID());
            s.setInt(6, event.getEntryID());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    ArrayList<EventModel> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<EventModel> list = new ArrayList<>();
        String sql = "SELECT entry_id, start_time, end_time, title, description FROM events WHERE client_id = ? AND start_time BETWEEN ? AND ?";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, super.getClientID());
            s.setInt(2, (int)t1.toEpochSecond(ZoneOffset.UTC));
            s.setInt(3, (int)t2.toEpochSecond(ZoneOffset.UTC));
            ResultSet set = s.executeQuery();
            while (set.next()){
                LocalDateTime tStart = LocalDateTime.ofInstant(Instant.ofEpochSecond(set.getInt("start_time")), TimeZone.getTimeZone("UTC").toZoneId());
                LocalDateTime tEnd = LocalDateTime.ofInstant(Instant.ofEpochSecond(set.getInt("end_time")), TimeZone.getTimeZone("UTC").toZoneId());
                list.add(new EventModel(set.getInt("entry_id"), tStart, tEnd, set.getString("title"), set.getString("description")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}

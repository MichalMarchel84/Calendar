package model.daos;

import model.App;
import model.models.EventModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EventDao extends EntryDao {

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
            stmt.setInt(3, toUnixTime(event.getStartTime()));
            stmt.setInt(4, toUnixTime(event.getEndTime()));
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
            s.setInt(1, toUnixTime(event.getStartTime()));
            s.setInt(2, toUnixTime(event.getEndTime()));
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

    List<EventModel> getBetween(LocalDateTime t1, LocalDateTime t2){
        ArrayList<EventModel> list = new ArrayList<>();
        String sql = "SELECT entry_id, start_time, end_time, title, description FROM events WHERE client_id = ? AND start_time BETWEEN ? AND ?";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, super.getClientID());
            s.setInt(2, toUnixTime(t1));
            s.setInt(3, toUnixTime(t2));
            ResultSet set = s.executeQuery();
            while (set.next()){
                list.add(new EventModel(set.getInt("entry_id"), toLocalTime(set.getInt("start_time")), toLocalTime(set.getInt("end_time")), set.getString("title"), set.getString("description")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
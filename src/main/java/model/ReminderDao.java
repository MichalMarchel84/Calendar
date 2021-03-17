package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ReminderDao extends EntryDao {

    ReminderDao(int clientID, Connection conn) {
        super("reminders", clientID, conn);
    }

    void create(ReminderModel r){
        String sql = "INSERT INTO reminders VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = super.getConn().prepareStatement(sql);
            stmt.setInt(1, super.getClientID());
            stmt.setInt(2, r.getEntryID());
            stmt.setInt(3, toUnixTime(r.getTime()));
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
            s.setInt(1, toUnixTime(r.getTime()));
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
            s.setInt(2, toUnixTime(t1));
            s.setInt(3, toUnixTime(t2));
            ResultSet set = s.executeQuery();
            while (set.next()){
                list.add(new ReminderModel(set.getInt("entry_id"), toLocalTime(set.getInt("time")), set.getString("title"), set.getString("description")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}

package model.daos;

import model.models.EntryModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class EntryDao {

    private final int clientID;
    private final Connection conn;
    private int lastID;
    private final String tableName;

    public EntryDao(String tableName, int clientID, Connection conn) {
        this.tableName = tableName;
        this.conn = conn;
        this.clientID = clientID;
        lastID = getLastID();
    }

    private int getLastID(){
        int id = 0;
        String sql ="SELECT MAX(entry_id) AS id FROM " + tableName + " WHERE client_id = ?";
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

    void delete(EntryModel entry){
        String sql = "DELETE FROM " + tableName + " WHERE client_id = ? AND entry_id = ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, clientID);
            s.setInt(2, entry.getEntryID());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getNextID(){
        lastID++;
        return lastID;
    }

    int toUnixTime(LocalDateTime t){
        return (int)t.toEpochSecond(ZoneOffset.UTC);
    }

    LocalDateTime toLocalTime(int t){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(t), TimeZone.getTimeZone("UTC").toZoneId());
    }

    public int getClientID() {
        return clientID;
    }

    public Connection getConn() {
        return conn;
    }
}

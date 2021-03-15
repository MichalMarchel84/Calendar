package model.daos;

import model.App;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RepetitiveDao extends EntryDao {

    private static final String[] types = {"repetitive_reminders", "repetitive_events"};
    private final int type;

    public RepetitiveDao(int type, int clientID, Connection conn) {
        super(types[type], clientID, conn);
        this.type = type;
    }

    public RepetitiveDao(int type, int clientID) {
        this(type, clientID, App.conn);
    }

    public ResultSet getInstancesBetween(LocalDateTime t1, LocalDateTime t2) throws SQLException {
        String sql = "SELECT * FROM " + types[type] + " WHERE started_at <= ? AND (finished_at IS NULL OR finished_at >= ?)";
        if(t1.isAfter(t2)){
            LocalDateTime temp = t1;
            t1 = t2;
            t2 = temp;
        }
        PreparedStatement s = super.getConn().prepareStatement(sql);
        s.setInt(1, toUnixTime(t2));
        s.setInt(2, toUnixTime(t1));
        return s.executeQuery();
    }
}

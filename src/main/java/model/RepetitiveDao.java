package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

abstract class RepetitiveDao extends EntryDao {

    private static final String[] types = {"repetitive_reminders", "repetitive_events"};
    private final int type;

    RepetitiveDao(int type, int clientID, Connection conn) {
        super(types[type], clientID, conn);
        this.type = type;
    }

    RepetitiveDao(int type, int clientID) {
        this(type, clientID, App.conn);
    }

    ArrayList<RepetitiveModel> getInstancesBetween(LocalDateTime t1, LocalDateTime t2) throws SQLException {
        String sql = "SELECT * FROM " + types[type] + " WHERE started_at <= ? AND (finished_at IS NULL OR finished_at >= ?)";
        if(t1.isAfter(t2)){
            LocalDateTime temp = t1;
            t1 = t2;
            t2 = temp;
        }
        PreparedStatement s = super.getConn().prepareStatement(sql);
        s.setInt(1, toUnixTime(t2));
        s.setInt(2, toUnixTime(t1));
        ResultSet set = s.executeQuery();
        ArrayList<RepetitiveModel> instances = new ArrayList<>();
        while (set.next()){
            int id = set.getInt("entry_id");
            LocalDateTime startedAt = toLocalTime(set.getInt("started_at"));
            LocalDateTime finishedAt;
            if(set.getInt("finished_at") != 0) {
                finishedAt = toLocalTime(set.getInt("finished_at"));
            }
            else {
                finishedAt = null;
            }
            String title = set.getString("title");
            String description = set.getString("description");
            int interval = set.getInt("interval");
            if(type == 0) {
                instances.add(new RepetitiveReminderModel(id, title, description, startedAt, finishedAt, interval));
            }
            else if(type == 1){
                long duration = set.getInt("duration");
                instances.add(new RepetitiveEventModel(id, title, description, startedAt, finishedAt, interval, duration));
            }
        }
        return instances;
    }
}

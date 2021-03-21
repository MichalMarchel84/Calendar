package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;

class RepetitiveEventDao extends RepetitiveDao{

    RepetitiveEventDao(int clientID, Connection conn) {
        super(1, clientID, conn);
    }

    void create(RepetitiveEventModel model) {
        String sql = "INSERT INTO repetitive_events VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, super.getClientID());
            s.setInt(2, model.getEntryID());
            s.setInt(3, toUnixTime(model.getTime()));
            if (model.getFinishedAt() == null) {
                s.setNull(4, Types.NULL);
            } else {
                s.setInt(4, toUnixTime(model.getFinishedAt()));
            }
            s.setInt(5, model.getInterval());
            s.setInt(6, (int) model.getDuration());
            s.setString(7, model.getTitle());
            s.setString(8, model.getDescription());
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void update(RepetitiveEventModel model){
        String sql = "UPDATE repetitive_events SET started_at = ?, finished_at = ?, interval = ?, duration = ?, title = ?, description = ?";
        try{
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, toUnixTime(model.getTime()));
            if (model.getFinishedAt() == null) {
                s.setNull(2, Types.NULL);
            } else {
                s.setInt(2, toUnixTime(model.getFinishedAt()));
            }
            s.setInt(3, model.getInterval());
            s.setInt(4, (int)model.getDuration());
            s.setString(5, model.getTitle());
            s.setString(6, model.getDescription());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}

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
            s.setInt(3, toUnixTime(model.getStartAt()));
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
        String sql = "UPDATE repetitive_events SET started_at = ?, finished_at = ?, interval = ?, end_time = ?, title = ?, description = ?";
        try{
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, toUnixTime(model.getStartAt()));
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

    ArrayList<RepetitiveEventModel> getBetween(LocalDateTime t1, LocalDateTime t2) {
        if (t1.isAfter(t2)) {
            LocalDateTime temp = t1;
            t1 = t2;
            t2 = temp;
        }
        ArrayList<RepetitiveEventModel> list = new ArrayList<>();
        try {
            //looking for instances valid for given period in DB
            ArrayList<RepetitiveModel> instances = (ArrayList<RepetitiveModel>) super.getInstancesBetween(t1, t2);

            //generating occurrences in given period
            for (RepetitiveModel model : instances) {
                RepetitiveEventModel m = (RepetitiveEventModel) model;
                LocalDateTime occurrenceTime = m.getFirstAfter(t1.minusMinutes(m.getDuration())); //including result when t1 after start but before end of event
                while ((occurrenceTime != null) && occurrenceTime.isBefore(t2.plusMinutes(1))) { //including result for t2
                    list.add(m.copy(occurrenceTime));
                    occurrenceTime = m.getFirstAfter(occurrenceTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

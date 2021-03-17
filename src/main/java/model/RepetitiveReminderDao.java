package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

class RepetitiveReminderDao extends RepetitiveDao{

    RepetitiveReminderDao(int clientID, Connection conn) {
        super(0, clientID, conn);
    }

    void create(RepetitiveReminderModel model){
        String sql = "INSERT INTO repetitive_reminders VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, super.getClientID());
            s.setInt(2, model.getEntryID());
            s.setInt(3, toUnixTime(model.getStartAt()));
            if(model.getFinishedAt() == null) {
                s.setNull(4, Types.NULL);
            }
            else {
                s.setInt(4, toUnixTime(model.getFinishedAt()));
            }
            s.setInt(5, model.getInterval());
            s.setString(6, model.getTitle());
            s.setString(7, model.getDescription());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    void update(RepetitiveReminderModel model){
        String sql = "UPDATE repetitive_reminders SET started_at = ?, finished_at = ?, interval = ?, title = ?, description = ?";
        try{
            PreparedStatement s = super.getConn().prepareStatement(sql);
            s.setInt(1, toUnixTime(model.getStartAt()));
            if(model.getFinishedAt() == null) {
                s.setNull(2, Types.NULL);
            }
            else {
                s.setInt(2, toUnixTime(model.getFinishedAt()));
            }
            s.setInt(3, model.getInterval());
            s.setString(4, model.getTitle());
            s.setString(5, model.getDescription());
            s.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    HashMap<Integer, ArrayList<RepetitiveReminderModel>> getBetween(LocalDateTime t1, LocalDateTime t2){
        if(t1.isAfter(t2)){
            LocalDateTime temp = t1;
            t1 = t2;
            t2 = temp;
        }
        HashMap<Integer, ArrayList<RepetitiveReminderModel>> result = new HashMap<>();
        try{
            //looking for instances valid for given period in DB
            ArrayList<RepetitiveModel> instances = super.getInstancesBetween(t1, t2);

            //generating occurrences in given period
            for(RepetitiveModel model : instances){
                RepetitiveReminderModel m = (RepetitiveReminderModel) model;
                ArrayList<RepetitiveReminderModel> list = m.getBetween(t1, t2);
                if(list.size() > 0){
                    result.put(Integer.valueOf(list.get(0).getEntryID()), list);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}

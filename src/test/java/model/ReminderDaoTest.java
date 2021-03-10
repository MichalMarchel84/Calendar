package model;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReminderDaoTest{

    private static Connection conn = TestMethods.getConnection();

    private boolean modelsEqual(ReminderModel r1, ReminderModel r2){
        boolean equal = (r1.getEntryID() == r2.getEntryID()) && (r1.getTitle().equals(r2.getTitle())) && (r1.getDescription().equals(r2.getDescription()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        if(!r1.getTime().format(dtf).equals(r2.getTime().format(dtf))){
            equal = false;
        }
        return equal;
    }

    @Before
    public void setUp(){
        conn = TestMethods.getConnection();
        TestMethods.clearDB(conn);
    }

    @After
    public void cleanup(){
        try {
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void whenGetLastID_thenReturns_highestID(){
        ReminderDao dao = new ReminderDao(1, conn);
        for(int i = 0; i < 5; i++){
            dao.insert(new ReminderModel(dao.getNextID(), LocalDateTime.now()));
        }
        Assert.assertEquals(5, dao.getLastID());
    }

    @Test
    public void whenGetBetween_thenReturns_remindersBetweenGivenDates(){
        LocalDateTime time = LocalDateTime.now();
        ReminderDao dao = new ReminderDao(1, conn);
        LocalDateTime t1 = time.minusDays(1);
        LocalDateTime t2 = time.plusDays(1);
        LocalDateTime actualTime = time.minusDays(2);
        ArrayList<ReminderModel> expected = new ArrayList<>();
        while (actualTime.isBefore(time.plusDays(2))){
            actualTime = actualTime.plusHours(1);
            ReminderModel r = new ReminderModel(dao.getNextID(), actualTime);
            dao.insert(r);
            if((actualTime.isAfter(t1) || actualTime.equals(t1)) && (actualTime.isBefore(t2) || actualTime.equals(t2))){
                expected.add(r);
            }
        }
        ArrayList<ReminderModel> returned = dao.getBetween(t1, t2);
        assertEquals(expected.size(), returned.size());
        boolean contentEqual = true;
        for (int i = 0; i < expected.size() - 1; i++){
            if(!modelsEqual(expected.get(i), returned.get(i))){
                contentEqual = false;
            }
        }
        assertTrue(contentEqual);
    }

}
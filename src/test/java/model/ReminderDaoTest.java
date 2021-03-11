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
        int rounds = 5;
        for(int i = 0; i < rounds; i++){
            dao.create(new ReminderModel(dao.getNextID(), LocalDateTime.now()));
        }
        Assert.assertEquals(rounds, dao.getLastID());
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
            dao.create(r);
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

    @Test
    public void whenDelete_thenGetBetweenReturns_listWithoutDeleted(){
        LocalDateTime time = LocalDateTime.now();
        ReminderDao dao = new ReminderDao(1, conn);
        ArrayList<ReminderModel> expected = new ArrayList<>();
        expected.add(new ReminderModel(dao.getNextID(), time));
        expected.add(new ReminderModel(dao.getNextID(), time.plusDays(1)));
        expected.add(new ReminderModel(dao.getNextID(), time.plusDays(2)));
        for (ReminderModel r : expected){
            dao.create(r);
        }
        dao.delete(expected.get(1));
        expected.remove(1);
        ArrayList<ReminderModel> returned = dao.getBetween(time, time.plusDays(2));
        boolean contentEqual = true;
        for (int i = 0; i < expected.size() - 1; i++){
            if(!modelsEqual(expected.get(i), returned.get(i))){
                contentEqual = false;
            }
        }
        assertTrue(contentEqual);
    }

    @Test
    public void whenUpdate_thenGetBetweenReturns_modifiedModel(){
        ReminderDao dao = new ReminderDao(1, conn);
        ReminderModel r = new ReminderModel(dao.getNextID(), LocalDateTime.now());
        dao.create(r);
        r.setTime(r.getTime().plusDays(1));
        r.setTitle("test");
        r.setDescription("test description");
        dao.update(r);
        ReminderModel returned = dao.getBetween(r.getTime(), r.getTime()).get(0);
        assertTrue(modelsEqual(r, returned));
    }
}
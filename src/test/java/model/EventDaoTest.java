package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventDaoTest{

    private Connection conn = TestMethods.getConnection();

    private boolean modelsEqual(EventModel e1, EventModel e2){
        boolean equal = (e1.getEntryID() == e2.getEntryID()) && (e1.getTitle().equals(e2.getTitle())) && (e1.getDescription().equals(e2.getDescription()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        if(!e1.getStartTime().format(dtf).equals(e2.getStartTime().format(dtf))){
            equal = false;
        }
        if(!e1.getEndTime().format(dtf).equals(e2.getEndTime().format(dtf))){
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
    public void whenGetBetween_thenReturns_remindersBetweenGivenDates(){
        LocalDateTime time = LocalDateTime.now();
        EventDao dao = new EventDao(1, conn);
        LocalDateTime t1 = time.minusDays(1);
        LocalDateTime t2 = time.plusDays(1);
        LocalDateTime actualTime = time.minusDays(2);
        ArrayList<EventModel> expected = new ArrayList<>();
        while (actualTime.isBefore(time.plusDays(2))){
            actualTime = actualTime.plusHours(1);
            EventModel em = new EventModel(dao.getNextID(), actualTime, actualTime.plusHours(1));
            dao.create(em);
            if((actualTime.isAfter(t1) || actualTime.equals(t1)) && (actualTime.isBefore(t2) || actualTime.equals(t2))){
                expected.add(em);
            }
        }
        ArrayList<EventModel> returned = dao.getBetween(t1, t2);
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
        EventDao dao = new EventDao(1, conn);
        ArrayList<EventModel> expected = new ArrayList<>();
        expected.add(new EventModel(dao.getNextID(), time, time.plusHours(1)));
        expected.add(new EventModel(dao.getNextID(), time.plusDays(1), time.plusDays(1).plusHours(1)));
        expected.add(new EventModel(dao.getNextID(), time.plusDays(2), time.plusDays(2).plusHours(1)));
        for (EventModel em : expected){
            dao.create(em);
        }
        dao.delete(expected.get(1));
        expected.remove(1);
        ArrayList<EventModel> returned = dao.getBetween(time, time.plusDays(2));
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
        EventDao dao = new EventDao(1, conn);
        EventModel em = new EventModel(dao.getNextID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        dao.create(em);
        em.setTime(em.getStartTime().plusDays(1), em.getEndTime().plusDays(1));
        em.setTitle("test");
        em.setDescription("test description");
        dao.update(em);
        EventModel returned = dao.getBetween(em.getStartTime(), em.getStartTime()).get(0);
        assertTrue(modelsEqual(em, returned));
    }

}
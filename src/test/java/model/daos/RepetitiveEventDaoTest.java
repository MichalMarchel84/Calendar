package model.daos;

import model.TestMethods;
import model.models.RepetitiveEventModel;
import model.models.RepetitiveModel;
import model.models.RepetitiveReminderModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RepetitiveEventDaoTest{

    private static Connection conn;

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
    public void getBetweenTest_forTwoDaysInterval(){

        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);

        TestMethods.setRepetitiveEventContent(conn, startedAt, 60, 2);

        RepetitiveEventDao dao = new RepetitiveEventDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2021, 01, 15, 12, 30);
        LocalDateTime t2 = LocalDateTime.of(2021, 01, 20, 12, 0);
        ArrayList<RepetitiveEventModel> result = dao.getBetween(t1, t2);
        assertEquals(3, result.size());
        assertEquals(startedAt, result.get(0).getTime());
    }

    @Test
    public void getBetweenTest_forMonthlyInterval(){
        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);

        TestMethods.setRepetitiveEventContent(conn, startedAt, 60, 0);

        RepetitiveEventDao dao = new RepetitiveEventDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2020, 01, 15, 12, 0);
        LocalDateTime t2 = LocalDateTime.of(2021, 06, 15, 12, 0);
        ArrayList<RepetitiveEventModel> result = dao.getBetween(t1, t2);
        assertEquals(6, result.size());
        assertEquals(startedAt, result.get(0).getTime());
    }

}
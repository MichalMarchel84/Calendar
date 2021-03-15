package model.daos;

import model.TestMethods;
import model.models.RepetitiveReminderModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RepetitiveReminderDaoTest{

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
    public void getBetweenTest_forTwoDaysInterval_entryNotFinished(){
        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);

        TestMethods.setRepetitiveReminderContent(conn, startedAt, null, 2);

        RepetitiveReminderDao dao = new RepetitiveReminderDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2020, 06, 15, 12, 0);
        LocalDateTime t2 = LocalDateTime.of(2021, 01, 20, 12, 0);
        ArrayList<RepetitiveReminderModel> result = dao.getBetween(t1, t2);
        assertEquals(3, result.size());
        assertEquals(startedAt, result.get(0).getTime());
    }

    @Test
    public void getBetweenTest_forTwoDaysInterval_entryFinished(){
        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);
        LocalDateTime finishedAt = startedAt.plusDays(6);

        TestMethods.setRepetitiveReminderContent(conn, startedAt, finishedAt, 2);

        RepetitiveReminderDao dao = new RepetitiveReminderDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2020, 06, 15, 12, 0);
        LocalDateTime t2 = LocalDateTime.of(2022, 06, 15, 12, 0);
        ArrayList<RepetitiveReminderModel> result = dao.getBetween(t1, t2);
        assertEquals(4, result.size());
        assertEquals(startedAt, result.get(0).getTime());
        assertEquals(finishedAt, result.get(3).getTime());
    }

    @Test
    public void getBetweenTest_forMonthlyInterval(){
        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);

        TestMethods.setRepetitiveReminderContent(conn, startedAt, null, 0);

        RepetitiveReminderDao dao = new RepetitiveReminderDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2021, 01, 15, 12, 0);
        LocalDateTime t2 = LocalDateTime.of(2021, 06, 15, 12, 0);
        ArrayList<RepetitiveReminderModel> result = dao.getBetween(t1, t2);
        assertEquals(6, result.size());
        assertEquals(startedAt, result.get(0).getTime());
    }

    @Test
    public void getBetweenTest_forYearlyInterval(){
        LocalDateTime startedAt = LocalDateTime.of(2021, 1, 15, 12, 0);

        TestMethods.setRepetitiveReminderContent(conn, startedAt, null, -1);

        RepetitiveReminderDao dao = new RepetitiveReminderDao(1, conn);
        LocalDateTime t1 = LocalDateTime.of(2021, 01, 16, 12, 0);
        LocalDateTime t2 = LocalDateTime.of(2023, 06, 15, 12, 0);
        ArrayList<RepetitiveReminderModel> result = dao.getBetween(t1, t2);
        assertEquals(2, result.size());
        assertEquals(startedAt.plusYears(1), result.get(0).getTime());
    }
}
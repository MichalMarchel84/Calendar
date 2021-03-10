package model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClientDaoTest {

    private static Connection conn = TestMethods.getConnection();

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
    public void whenNewUserAdded_thenUserID_equals1(){
        ClientDao client = null;
        try {
            client = new ClientDao("test1", "1234", true, conn);
        }
        catch (LoginPanelException e){
            e.printStackTrace();
        }
        assertEquals(1, client.getID());
    }

    @Test
    public void whenNextUserAdded_thenUserID_equals2(){
        ClientDao client = null;
        try {
            new ClientDao("test1", "1234", true, conn);
            client = new ClientDao("test2", "1234", true, conn);
        }
        catch (LoginPanelException e){
            e.printStackTrace();
        }
        assertEquals(2, client.getID());
    }

    @Test
    public void whenDuplicateUserAdded_thenConstructor_throwsException(){
        try {
            new ClientDao("test2", "1234", true, conn);
            new ClientDao("test2", "1234", true, conn);
            fail("LoginPanelException not thrown");
        }
        catch (LoginPanelException e){
            assertEquals("error_login_in_use", e.getMessage());
        }
    }
}
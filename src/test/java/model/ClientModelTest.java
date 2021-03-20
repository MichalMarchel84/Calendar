package model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClientModelTest {

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
    public void whenDuplicateClientAdded_thenThrows_LoginPanelException(){
        String login = "test";
        String pass = "1234";
        try {
            new Client(conn, login, pass, true);
            new Client(conn, login, pass, true);
            fail("No exception thrown");
        }
        catch (LoginException e){
            Assert.assertEquals("error_login_in_use", e.getMessage());
        }
    }

    @Test
    public void whenLoginMissing_thenThrows_LoginPanelException(){
        try {
            new Client(conn, "test", "1234", false);
            fail("No exception thrown");
        }
        catch (LoginException e){
            assertEquals("error_wrong_login", e.getMessage());
        }
    }

    @Test
    public void whenWrongPassword_thenThrows_LoginPanelException(){
        try {
            new Client(conn, "test", "12345", true);
            new Client(conn, "test", "1234", false);
            fail("No exception thrown");
        }
        catch (LoginException e){
            assertEquals("error_wrong_password", e.getMessage());
        }
    }

    @Test
    public void whenAllOk_thenNewClientCreated(){
        String login = "test";
        String pass = "1234";
        try {
            Client expected = new Client(conn, login, pass, true);
            Client result = new Client(conn, login, pass, false);
            assertEquals(expected.getClientId(), result.getClientId());
        }
        catch (LoginException e){
            e.printStackTrace();
        }
    }
}
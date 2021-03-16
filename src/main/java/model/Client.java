package model;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.time.LocalDateTime;

public class Client {

    private final ClientDao clientDao;
    private final ReminderDao reminderDao;
    private final EventDao eventDao;
    private final RepetitiveReminderDao repetitiveReminderDao;
    private final RepetitiveEventDao repetitiveEventDao;

    public Client(Connection conn, String login, String pass, boolean newClient) throws LoginPanelException {
        if(newClient){
            clientDao = new ClientDao(conn, login, hash(pass));
        }
        else {
            clientDao = new ClientDao(conn, login);
            if(!verifyHash(login, pass)){
                throw new LoginPanelException("error_wrong_password");
            }
        }
        reminderDao = new ReminderDao(clientDao.getID(), conn);
        eventDao = new EventDao(clientDao.getID(), conn);
        repetitiveReminderDao = new RepetitiveReminderDao(clientDao.getID(), conn);
        repetitiveEventDao = new RepetitiveEventDao(clientDao.getID(), conn);
    }

    public Client(String login, String pass, boolean newClient) throws LoginPanelException{
        this(App.conn, login, pass, newClient);
    }

    public Client(String login, String pass) throws LoginPanelException{
        this(App.conn, login, pass, false);
    }

    private String hash(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt(10));
    }

    private boolean verifyHash(String login, String pass) throws LoginPanelException{
        return BCrypt.checkpw(pass, clientDao.getPasswordHash(login));
    }

    public int getClientId(){
        return clientDao.getID();
    }

    public ReminderModel createReminder(LocalDateTime time, String title, String description){
        ReminderModel result = new ReminderModel(reminderDao.getNextID(), time, title, description);
        reminderDao.create(result);
        return result;
    }

    public EventModel createEvent(LocalDateTime timeStart, LocalDateTime timeEnd, String title, String description){
        EventModel result = new EventModel(eventDao.getNextID(), timeStart, timeEnd, title, description);
        eventDao.create(result);
        return result;
    }

    public RepetitiveReminderModel createRepetitiveReminder(LocalDateTime startedAt, int interval, String title, String description){
        RepetitiveReminderModel result = new RepetitiveReminderModel(repetitiveReminderDao.getNextID(), title, description, null, startedAt, interval);
        repetitiveReminderDao.create(result);
        return result;
    }
}

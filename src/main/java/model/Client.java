package model;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

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
        RepetitiveReminderModel result = new RepetitiveReminderModel(repetitiveReminderDao.getNextID(), title, description, startedAt, null, interval);
        repetitiveReminderDao.create(result);
        return result;
    }

    public RepetitiveEventModel createRepetitiveEvent(LocalDateTime startedAt, long duration, int interval, String title, String description){
        RepetitiveEventModel result = new RepetitiveEventModel(repetitiveEventDao.getNextID(), title, description, startedAt, null, interval, duration);
        repetitiveEventDao.create(result);
        return result;
    }

    public void update(EntryModel model){
        if(model instanceof ReminderModel){
            reminderDao.update((ReminderModel) model);
        }
        else if(model instanceof EventModel){
            eventDao.update((EventModel) model);
        }
        else if(model instanceof RepetitiveReminderModel){
            repetitiveReminderDao.update((RepetitiveReminderModel) model);
        }
        else if(model instanceof RepetitiveEventModel){
            repetitiveEventDao.update((RepetitiveEventModel) model);
        }
    }

    public void delete(EntryModel model){
        if(model instanceof ReminderModel){
            reminderDao.delete(model);
        }
        else if(model instanceof EventModel){
            eventDao.delete(model);
        }
        else if(model instanceof RepetitiveReminderModel){
            repetitiveReminderDao.delete(model);
        }
        else if(model instanceof RepetitiveEventModel){
            repetitiveEventDao.delete(model);
        }
    }

    public ArrayList<ReminderModel> getRemindersBetween(LocalDateTime t1, LocalDateTime t2){
        return reminderDao.getBetween(t1, t2);
    }

    public ArrayList<EventModel> getEventsBetween(LocalDateTime t1, LocalDateTime t2){
        return eventDao.getBetween(t1, t2);
    }

    public HashMap<Integer, ArrayList<RepetitiveReminderModel>> getRepetitiveRemindersBetween(LocalDateTime t1, LocalDateTime t2){
        return repetitiveReminderDao.getBetween(t1, t2);
    }
}

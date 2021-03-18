package model;

import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EntryModelTest{



    @Test
    public void whenModelChange_thenModel_firesActionEvent(){

        class Listener implements ActionListener {

            int lastEventID = -1;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lastEventID = actionEvent.getID();
            }
        }

        Listener listener = new Listener();
        EntryModel model = new EntryModel(1, LocalDateTime.now(), null, null) {};
        model.addActionListener(listener);
        model.setTime(LocalDateTime.now());
        assertEquals(1, listener.lastEventID);
        model.setTitle("test");
        assertEquals(2, listener.lastEventID);
        model.setDescription("test desc");
        assertEquals(3, listener.lastEventID);
    }

    @Test
    public void transactionTest(){

        class Listener implements ActionListener {

            int events = 0;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                events++;
                if(actionEvent.getID() != 0){
                    fail("Event id not as expected");
                }
            }
        }

        Listener listener = new Listener();
        EntryModel model = new EntryModel(1, LocalDateTime.now(), null, null) {};
        model.addActionListener(listener);
        model.beginTransaction();
        model.setTime(LocalDateTime.now());
        model.setTitle("test");
        model.setDescription("test desc");
        model.commit();
        assertEquals(1, listener.events);
    }
}
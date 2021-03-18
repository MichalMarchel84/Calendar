package model;

import java.time.LocalDateTime;

public class ReminderModel extends EntryModel {

    ReminderModel(int entryID, LocalDateTime time, String title, String description) {
        super(entryID, time, title, description);
    }
}

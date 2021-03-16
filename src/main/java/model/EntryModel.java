package model;

public class EntryModel {

    private final int entryID;
    private String title;
    private String description;

    EntryModel(int entryID, String title, String description) {
        this.entryID = entryID;
        this.title = title;
        this.description = description;
    }

    public int getEntryID() {
        return entryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

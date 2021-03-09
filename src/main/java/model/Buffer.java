package model;

public class Buffer {

    private final int clientID;
    private static int lastEntryID;

    public Buffer() {
        clientID = getClientID();
        lastEntryID = getLastEntryID();
    }

    static int getNextID(){
        lastEntryID++;
        return lastEntryID;
    }

    private int getClientID(){
        return -1;
    }

    private int getLastEntryID(){
        return 0;
    }
}

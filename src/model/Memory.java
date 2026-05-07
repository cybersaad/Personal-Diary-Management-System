package model;

import util.FileUtil;

/**
 * Represents a personal memory with a title, date, description, and location.
 * Stored as a Base64-encoded pipe-delimited line in memories.txt.
 */
public class Memory {
    private String title;
    private String date;
    private String description;
    private String location;

    /**
     * Creates a new Memory.
     *
     * @param title       the memory title
     * @param date        the date in YYYY-MM-DD format
     * @param description a text description of the memory
     * @param location    where the memory took place
     */
    public Memory(String title, String date, String description, String location) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.location = location;
    }

    public String getTitle()       { return title; }
    public String getDate()        { return date; }
    public String getDescription() { return description; }
    public String getLocation()    { return location; }

    @Override
    public String toString() {
        return title + " (" + date + ")";
    }

    /** Serializes this memory to a pipe-delimited, Base64-encoded line for file storage. */
    public String toFileString() {
        return FileUtil.encodeLine(title, date, description, location);
    }
}

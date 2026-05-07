package model;

import util.FileUtil;

/**
 * Represents a mood log entry with the selected mood, date, and an optional note.
 * Stored as a Base64-encoded pipe-delimited line in mood.txt.
 */
public class MoodEntry {
    private Mood mood;
    private String date;
    private String note;

    /**
     * Creates a new mood entry.
     *
     * @param mood the selected mood
     * @param date the date in YYYY-MM-DD format
     * @param note an optional note about how the user feels
     */
    public MoodEntry(Mood mood, String date, String note) {
        this.mood = mood;
        this.date = date;
        this.note = note;
    }

    public Mood   getMood() { return mood; }
    public String getDate() { return date; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return mood + " (" + date + ")";
    }

    /** Serializes this entry to a pipe-delimited, Base64-encoded line for file storage. */
    public String toFileString() {
        return FileUtil.encodeLine(mood.name(), date, note);
    }
}

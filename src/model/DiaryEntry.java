package model;

import util.FileUtil;

/**
 * Represents a single diary entry with a title, date, content, and optional mood.
 * Each entry is stored as a Base64-encoded pipe-delimited line in diary.txt.
 */
public class DiaryEntry {
    private String title;
    private String date;
    private String content;
    private Mood mood;

    /**
     * Creates a diary entry with all fields including mood.
     *
     * @param title   the entry title
     * @param date    the entry date in YYYY-MM-DD format
     * @param content the diary text
     * @param mood    the mood associated with this entry (can be null)
     */
    public DiaryEntry(String title, String date, String content, Mood mood) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.mood = mood;
    }

    /** Creates a diary entry without a mood (backward-compatible). */
    public DiaryEntry(String title, String date, String content) {
        this(title, date, content, null);
    }

    public String getTitle()   { return title; }
    public String getDate()    { return date; }
    public String getContent() { return content; }
    public Mood   getMood()    { return mood; }

    public void setMood(Mood mood) { this.mood = mood; }

    @Override
    public String toString() {
        String moodSuffix = (mood != null) ? " — " + mood : "";
        return title + " (" + date + ")" + moodSuffix;
    }

    /** Serializes this entry to a pipe-delimited, Base64-encoded line for file storage. */
    public String toFileString() {
        String moodValue = (mood != null) ? mood.name() : "";
        return FileUtil.encodeLine(title, date, content, moodValue);
    }
}

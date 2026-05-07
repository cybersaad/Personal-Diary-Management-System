package model;

import util.FileUtil;

/**
 * Represents a task with a title, deadline, priority level, and completion status.
 * Stored as a Base64-encoded pipe-delimited line in tasks.txt.
 */
public class Task {
    private String title;
    private String deadline;
    private Priority priority;
    private boolean completed;

    /**
     * Creates a new incomplete task.
     *
     * @param title    the task title
     * @param deadline the deadline date in YYYY-MM-DD format
     * @param priority the priority level (HIGH, MEDIUM, LOW)
     */
    public Task(String title, String deadline, Priority priority) {
        this.title = title;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = false;
    }

    public String   getTitle()    { return title; }
    public String   getDeadline() { return deadline; }
    public Priority getPriority() { return priority; }
    public boolean  isCompleted() { return completed; }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        String status = completed ? "\u2714 " : "";
        return status + title + "  |  " + priority + "  |  " + deadline;
    }

    /** Serializes this task to a pipe-delimited, Base64-encoded line for file storage. */
    public String toFileString() {
        return FileUtil.encodeLine(title, deadline, priority.name(), String.valueOf(completed));
    }
}

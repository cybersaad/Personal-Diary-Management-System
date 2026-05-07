package manager;

import model.Priority;
import model.Task;
import util.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages reading, writing, and updating tasks.
 * Data is persisted to tasks.txt in the application's data directory.
 */
public class TaskManager {
    private static final String FILE = "tasks.txt";

    /** Appends a new task to the file. */
    public void addTask(Task task) {
        FileUtil.write(FILE, task.toFileString());
    }

    /** Reads all tasks from the file. */
    public List<Task> getAllTasks() {
        List<String> lines = FileUtil.read(FILE);
        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            String[] fields = FileUtil.decodeLine(line);
            if (fields.length == 4) {
                try {
                    Task task = new Task(
                            fields[0],
                            fields[1],
                            Priority.valueOf(fields[2]));
                    task.setCompleted(Boolean.parseBoolean(fields[3]));
                    tasks.add(task);
                } catch (IllegalArgumentException ignored) {
                    // Skip malformed task entries
                }
            }
        }
        return tasks;
    }

    /** Overwrites the file with the given list of tasks. */
    public void saveAll(List<Task> tasks) {
        List<String> data = new ArrayList<>();
        for (Task task : tasks) {
            data.add(task.toFileString());
        }
        FileUtil.overwrite(FILE, data);
    }
}

package manager;

import model.DiaryEntry;
import model.Mood;
import util.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages reading, writing, and updating diary entries.
 * Data is persisted to diary.txt in the application's data directory.
 */
public class DiaryManager {
    private static final String FILE = "diary.txt";

    /** Appends a new diary entry to the file. */
    public void addEntry(DiaryEntry entry) {
        FileUtil.write(FILE, entry.toFileString());
    }

    /** Reads all diary entries from the file. */
    public List<DiaryEntry> getAllEntries() {
        List<String> lines = FileUtil.read(FILE);
        List<DiaryEntry> entries = new ArrayList<>();
        for (String line : lines) {
            String[] fields = FileUtil.decodeLine(line);
            if (fields.length >= 3) {
                Mood mood = null;
                if (fields.length >= 4 && !fields[3].isEmpty()) {
                    try {
                        mood = Mood.valueOf(fields[3]);
                    } catch (IllegalArgumentException ignored) {}
                }
                entries.add(new DiaryEntry(fields[0], fields[1], fields[2], mood));
            }
        }
        return entries;
    }

    /** Updates the entry at the given index, then re-saves the entire file. */
    public void updateEntry(int index, DiaryEntry updated) {
        List<DiaryEntry> entries = getAllEntries();
        if (index >= 0 && index < entries.size()) {
            entries.set(index, updated);
            saveAll(entries);
        }
    }

    /** Overwrites the file with the given list of entries. */
    public void saveAll(List<DiaryEntry> entries) {
        List<String> data = new ArrayList<>();
        for (DiaryEntry entry : entries) {
            data.add(entry.toFileString());
        }
        FileUtil.overwrite(FILE, data);
    }
}

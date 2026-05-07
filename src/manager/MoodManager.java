package manager;

import model.Mood;
import model.MoodEntry;
import util.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages reading, writing, and analyzing mood entries.
 * Data is persisted to mood.txt in the application's data directory.
 */
public class MoodManager {
    private static final String FILE = "mood.txt";

    /** Appends a new mood entry to the file. */
    public void addMood(MoodEntry entry) {
        FileUtil.write(FILE, entry.toFileString());
    }

    /** Reads all mood entries from the file. */
    public List<MoodEntry> getAllMoods() {
        List<String> lines = FileUtil.read(FILE);
        List<MoodEntry> moods = new ArrayList<>();
        for (String line : lines) {
            String[] fields = FileUtil.decodeLine(line);
            if (fields.length == 3) {
                try {
                    moods.add(new MoodEntry(
                            Mood.valueOf(fields[0]),
                            fields[1],
                            fields[2]));
                } catch (IllegalArgumentException ignored) {
                    // Skip malformed mood entries
                }
            }
        }
        return moods;
    }

    /**
     * Analyzes all logged moods and returns the most frequently occurring one.
     *
     * @return the most frequent Mood, or null if no moods are logged
     */
    public Mood getMostFrequentMood() {
        List<MoodEntry> moods = getAllMoods();
        if (moods.isEmpty()) return null;

        Map<Mood, Integer> frequencyMap = new HashMap<>();
        for (MoodEntry entry : moods) {
            frequencyMap.merge(entry.getMood(), 1, Integer::sum);
        }

        Mood mostFrequent = null;
        int highestCount = 0;
        for (Map.Entry<Mood, Integer> pair : frequencyMap.entrySet()) {
            if (pair.getValue() > highestCount) {
                highestCount = pair.getValue();
                mostFrequent = pair.getKey();
            }
        }
        return mostFrequent;
    }
}

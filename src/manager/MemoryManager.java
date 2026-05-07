package manager;

import model.Memory;
import util.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages reading, writing, and persisting personal memories.
 * Data is persisted to memories.txt in the application's data directory.
 */
public class MemoryManager {
    private static final String FILE = "memories.txt";

    /** Appends a new memory to the file. */
    public void addMemory(Memory memory) {
        FileUtil.write(FILE, memory.toFileString());
    }

    /** Reads all memories from the file. */
    public List<Memory> getAllMemories() {
        List<String> lines = FileUtil.read(FILE);
        List<Memory> memories = new ArrayList<>();
        for (String line : lines) {
            String[] fields = FileUtil.decodeLine(line);
            if (fields.length == 4) {
                memories.add(new Memory(fields[0], fields[1], fields[2], fields[3]));
            }
        }
        return memories;
    }

    /** Overwrites the file with the given list of memories. */
    public void saveAll(List<Memory> memories) {
        List<String> data = new ArrayList<>();
        for (Memory memory : memories) {
            data.add(memory.toFileString());
        }
        FileUtil.overwrite(FILE, data);
    }
}

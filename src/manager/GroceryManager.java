package manager;

import model.GroceryItem;
import util.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages reading, writing, and updating grocery items.
 * Data is persisted to grocery.txt in the application's data directory.
 */
public class GroceryManager {
    private static final String FILE = "grocery.txt";

    /** Appends a new grocery item to the file. */
    public void addItem(GroceryItem item) {
        FileUtil.write(FILE, item.toFileString());
    }

    /** Reads all grocery items from the file. */
    public List<GroceryItem> getAllItems() {
        List<String> lines = FileUtil.read(FILE);
        List<GroceryItem> items = new ArrayList<>();
        for (String line : lines) {
            String[] fields = FileUtil.decodeLine(line);
            if (fields.length == 2) {
                GroceryItem item = new GroceryItem(fields[0]);
                item.setPurchased(Boolean.parseBoolean(fields[1]));
                items.add(item);
            }
        }
        return items;
    }

    /** Overwrites the file with the given list of items. */
    public void saveAll(List<GroceryItem> items) {
        List<String> data = new ArrayList<>();
        for (GroceryItem item : items) {
            data.add(item.toFileString());
        }
        FileUtil.overwrite(FILE, data);
    }
}

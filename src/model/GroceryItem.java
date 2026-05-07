package model;

import util.FileUtil;

/**
 * Represents a grocery shopping item with a purchased flag.
 * Stored as a Base64-encoded pipe-delimited line in grocery.txt.
 */
public class GroceryItem {
    private String name;
    private boolean purchased;

    /**
     * Creates a new unpurchased grocery item.
     *
     * @param name the name of the grocery item
     */
    public GroceryItem(String name) {
        this.name = name;
        this.purchased = false;
    }

    public String  getName()      { return name; }
    public boolean isPurchased()  { return purchased; }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return (purchased ? "\u2714 " : "") + name;
    }

    /** Serializes this item to a pipe-delimited, Base64-encoded line for file storage. */
    public String toFileString() {
        return FileUtil.encodeLine(name, String.valueOf(purchased));
    }
}

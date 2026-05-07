package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;

/**
 * Utility class for file I/O operations.
 * Uses Base64 encoding per field to prevent delimiter collisions.
 * All file paths are resolved relative to the application's working directory.
 */
public class FileUtil {

    // Resolve data directory relative to where the app runs (project root / data folder)
    private static final Path DATA_DIR;

    static {
        // Store data files in a "data" subdirectory next to the running jar/class
        Path base = Paths.get(System.getProperty("user.dir"));
        DATA_DIR = base.resolve("data");
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Resolves a filename to the data directory */
    public static String resolvePath(String filename) {
        return DATA_DIR.resolve(filename).toString();
    }

    /** Encode a single field to Base64 so it can never contain '|' or newlines */
    public static String encodeField(String field) {
        if (field == null) field = "";
        return Base64.getEncoder().encodeToString(field.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /** Decode a single Base64-encoded field back to plain text */
    public static String decodeField(String encoded) {
        if (encoded == null || encoded.isEmpty()) return "";
        try {
            return new String(Base64.getDecoder().decode(encoded), java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            // If it's not valid Base64 (legacy data), return as-is
            return encoded;
        }
    }

    /** Encode an array of fields into a single pipe-delimited line with Base64 fields */
    public static String encodeLine(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(encodeField(fields[i]));
        }
        return sb.toString();
    }

    /** Decode a pipe-delimited line into an array of plain-text fields */
    public static String[] decodeLine(String line) {
        if (line == null || line.trim().isEmpty()) return new String[0];
        String[] encoded = line.split("\\|", -1);
        String[] decoded = new String[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            decoded[i] = decodeField(encoded[i]);
        }
        return decoded;
    }

    /** Append a single line to a file */
    public static void write(String file, String data) {
        String path = resolvePath(file);
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Read all lines from a file */
    public static List<String> read(String file) {
        String path = resolvePath(file);
        List<String> list = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) return list;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    list.add(line);
                }
            }
        } catch (Exception e) {
            // file doesn't exist yet – return empty
        }
        return list;
    }

    /** Overwrite a file with a list of lines */
    public static void overwrite(String file, List<String> data) {
        String path = resolvePath(file);
        try (FileWriter fw = new FileWriter(path)) {
            for (String line : data) {
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates a date string matches YYYY-MM-DD format with valid ranges.
     * Returns true if valid, false otherwise.
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) return false;
        String trimmed = date.trim();
        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            String[] parts = trimmed.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            if (year < 1900 || year > 2100) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            // Basic month-day validation
            int[] daysInMonth = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            return day <= daysInMonth[month];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Returns today's date in YYYY-MM-DD format */
    public static String todayDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return today.toString(); // ISO format YYYY-MM-DD
    }
}

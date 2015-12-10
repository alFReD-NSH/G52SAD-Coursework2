package com.github.alFReDNSH.imageViewer;

import javafx.scene.control.Alert;

import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Util {
    private static DateTimeFormatter todayPattern = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter yearPattern = DateTimeFormatter.ofPattern("MMM d");
    private static DateTimeFormatter allTimePattern =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Will format the given time, relative to the current time.
     * If it was today, it will show the hour, if it was the year, it will show the day
     * and month else it'll show the day, month and the year.
     * @param milliseconds since epoch
     * @return
     */
    public static String formatDate(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        LocalDateTime lastModified = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        LocalDateTime today = LocalDateTime.now().minusHours(24);
        LocalDateTime year = LocalDateTime.now().minusYears(1);
        return lastModified.format(
                today.isBefore(lastModified) ? todayPattern :
                year.isBefore(lastModified) ? yearPattern :
                allTimePattern
        );

    }

    /**
     * Will format the bytes into human readable format like 20MB
     * @param bytes
     * @param si If true will use the si units(KB = 1000 B) else will use binary units
     *           (kB = 1024)
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Will show an alert to the user.
     * @param e
     * @param action  eg. `copying the file`
     */
    public static void handleException(Exception e, String action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("There was an error " + action + ".");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}

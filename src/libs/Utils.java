package libs;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;


public class Utils {
    private List<String> defaultKeys;
    private List<String> defaultValues;

    public Utils() {
        this.defaultKeys = List.of("title", "description", "startDate", "endDate", "participants",
                "lastModified", "organizer", "repeat", "frequency", "location", "until");
        this.defaultValues = List.of("My Event", "This is my event", "2020 05 29 0600", "2020 05 29 0700", "1",
                "2020 05 29 0600", "User", "false", "", "", "");
    }

    // TODO: This needs to be tested
    public static String getDefaultStartDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd HHmm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    // TODO: Return the current date + 1 hour as a string
    public static String getDefaultEndDate() {
        return "";
    }

    // TODO: This needs to be tested
    public List<String> getDefaultKeys() {
        return this.defaultKeys;
    }

    // TODO: This needs to be tested
    public List<String> getDefaultValues() {
        return this.defaultValues;
    }
}

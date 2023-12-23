package alp_09_22;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        // Create a map to store values associated with dates
        Map<LocalDate, String> calendarData = new HashMap<>();

        // Store values for specific dates
        calendarData.put(LocalDate.of(2023, 1, 1), "New Year's Day");
        calendarData.put(LocalDate.of(2023, 2, 14), "Valentine's Day");
        calendarData.put(LocalDate.of(2023, 7, 4), "Independence Day");
        // ... add more entries as needed

        // Search for a value associated with a specific date
        LocalDate searchDate = LocalDate.of(2023, 2, 14);
        String eventData = calendarData.get(searchDate);

        // Check if the date has associated data
        if (eventData != null) {
            System.out.println("Event on " + searchDate + ": " + eventData);
        } else {
            System.out.println("No event found for " + searchDate);
        }
    }
}

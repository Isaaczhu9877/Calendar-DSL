package libs;

// TODO
// 1) Some function that takes String date format and outputs Date object that Biweekly can use
// 2) function that exports the calendarEvents into a iCal object that is then written to ics file.
// 3)

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Calendar {
    private static final String JSON_ROOT_KEY  = "events";
    public static final String CALENDAR_FILE_PATH  = "src/disk/calendar.json";
    ArrayList<CalendarEvent> calendarEvents; // all Events for Calendar


    public Calendar() {
        this.calendarEvents = new ArrayList<>();
    }

    public Calendar(String filePath) {
        // TODO: Initialize calendarEvents from disk scan
        this.calendarEvents = loadFromDisk(filePath);
    }

    public ArrayList<CalendarEvent> loadFromDisk() {
        return this.loadFromDisk(null);
    }

        // TODO: Scan from disk on load if exists src/disk/calendar.json
    public ArrayList<CalendarEvent> loadFromDisk(String filePath) {
        ArrayList<CalendarEvent> diskCalendarEvents = new ArrayList<>();
        // Attempt to load
        // if file does not already exist at path, then create the file only
        // if file exists at path, load from it

        try {
            JSONParser parser = new JSONParser();
            File file = new File(filePath);
            if (file.exists()) {
                JSONObject diskObj = (JSONObject) parser.parse(new FileReader(filePath));
                JSONArray arr = (JSONArray) diskObj.get(JSON_ROOT_KEY);
                for (Object o : arr) {
                    diskCalendarEvents.add(CalendarEvent.fromJSON(o));
                }
            } else {
                file.createNewFile();
            }
            // convert each json object to a CalendarEvent obj
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diskCalendarEvents;
    }

    public boolean saveToDisk() {
        return this.saveToDisk(null);
    }

    // TODO: Save to disk in src/disk/calendar.json, either overwriting existing json, or creating a new one
    // This is the fn that would be called after each create, change, delete
    public boolean saveToDisk(String filePath) {
        try {
            JSONArray arr = new JSONArray();
            if (filePath == null)
                filePath = CALENDAR_FILE_PATH;
            File file = new File(filePath);
            if (!file.isFile())
                file.createNewFile();
            calendarEvents.forEach(calendarEvent -> arr.add(calendarEvent.toJSON()));
            FileWriter fileWriter = new FileWriter(filePath);

            JSONObject objToSave = new JSONObject();
            objToSave.put("events", arr);
            fileWriter.write(objToSave.toJSONString());
            fileWriter.close();
            System.out.println("JSON Object successfully written to the file!!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // For testing purposes
    public void stringifyCalendar() {
        int n = 1;
        System.out.println("Calendar: ");
        for (CalendarEvent event: calendarEvents) {
            System.out.println("Event " + n);
            System.out.println(event.stringifyEvent());
            System.out.println('\n');
            n++;
        }
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public ArrayList<CalendarEvent> findEvent(HashMap<String, String> searchDetails) {

        ArrayList<CalendarEvent> foundEvents = new ArrayList<>();
        for (CalendarEvent eventInMem : calendarEvents) {
            boolean b = true;
            for (String key : searchDetails.keySet()) {
                if(!searchDetails.get(key).equals(eventInMem.getEventAttribute(key))) {
                    b = false;
                }
            }
            if (b) {
                foundEvents.add(eventInMem);
            }
        }
        return foundEvents;
    }

    // TODO Create Event
    // Input: A set of key-value pairs. Need to figure out how to go from a string input to DateTime
    // Modifies: Creates an object of type event, add to calendarEvents.
    // Fields will either be of parsed values or default values.

    public void createEvent(HashMap<String, String> eventDetails) {
        CalendarEvent newEvent = new CalendarEvent();

        for (String key : eventDetails.keySet()) {
            newEvent.setEventAttribute(key, eventDetails.get(key));
        }

        if (newEvent.getEventAttribute("repeat").equals("true")) {
            expandEvent(newEvent);

        } else {
            calendarEvents.add(newEvent);
        }

        this.stringifyCalendar();
        this.saveToDisk();
    }

    private void expandEvent(CalendarEvent newEvent) {

        String frequency = newEvent.getEventAttribute("frequency");
        LocalDateTime untilDate = stringToDateTime(newEvent.getEventAttribute("until"));
        LocalDateTime startDate = stringToDateTime(newEvent.getEventAttribute("startDate"));
        LocalDateTime endDate = stringToDateTime(newEvent.getEventAttribute("endDate"));

        switch(frequency) {
            case "daily" -> dailyIterator(startDate, endDate, untilDate, newEvent);
            case "weekly" -> weeklyIterator(startDate, endDate, untilDate, newEvent);
            case "monthly" -> monthlyIterator(startDate, endDate, untilDate, newEvent);
            case "yearly" -> yearlyIterator(startDate, endDate, untilDate, newEvent);
        }

    }

    private void dailyIterator(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime untilDate, CalendarEvent newEvent){

        LocalDateTime finish = endDate;

        for (LocalDateTime begin = startDate; begin.isBefore(untilDate); begin = begin.plusDays(1), finish = finish.plusDays(1)) {
            createCurrentEvent(newEvent, begin, finish);
        }
    }

    private void weeklyIterator(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime untilDate, CalendarEvent newEvent){

        LocalDateTime finish = endDate;

        for (LocalDateTime begin = startDate; begin.isBefore(untilDate); begin = begin.plusWeeks(1), finish = finish.plusWeeks(1)) {
            createCurrentEvent(newEvent, begin, finish);
        }
    }

    private void monthlyIterator(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime untilDate, CalendarEvent newEvent){

        LocalDateTime finish = endDate;

        for (LocalDateTime begin = startDate; begin.isBefore(untilDate); begin = begin.plusMonths(1), finish = finish.plusMonths(1)) {
            createCurrentEvent(newEvent, begin, finish);
        }
    }

    private void yearlyIterator(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime untilDate, CalendarEvent newEvent){

        LocalDateTime finish = endDate;

        for (LocalDateTime begin = startDate; begin.isBefore(untilDate); begin = begin.plusYears(1), finish = finish.plusYears(1)) {
            createCurrentEvent(newEvent, begin, finish);
        }
    }

    private LocalDateTime stringToDateTime(String eventDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedEventDate = eventDate.substring(0, 4) + "-" + eventDate.substring(5,7) +
                "-" + eventDate.substring(8, 10) + " " + eventDate.substring(11, 13) + ":" + eventDate.substring(13);

        return LocalDateTime.parse(formattedEventDate, formatter);
    }

    private String dateTimeToString(LocalDateTime eventDate) {

        String eventString = eventDate.toString();
        String reformattedEventString = eventString.substring(0, 4) + " " + eventString.substring(5, 7) + " " +
                eventString.substring(8, 10) + " " + eventString.substring(11, 13) + eventString.substring(14);
        return reformattedEventString;
    }

    private void createCurrentEvent(CalendarEvent newEvent, LocalDateTime begin, LocalDateTime finish) {

        CalendarEvent current = new CalendarEvent();
        current.clone(newEvent);
        String start = dateTimeToString(begin);
        String end = dateTimeToString(finish);
        current.setEventAttribute("startDate", start);
        current.setEventAttribute("endDate", end);
        current.setEventAttribute("repeat", "false");
        current.setEventAttribute("frequency", "");
        current.setEventAttribute("until", "");
        calendarEvents.add(current);
    }


    // TODO
    public void changeEvent(HashMap<String, String> searchDetails, HashMap<String, String> modifyDetails) {
        // CalendarEvent eventToModify = findEvent(searchDetails);
        ArrayList<CalendarEvent> eventsToModify = findEvent(searchDetails);
        SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd HHmm");
        Date now = new Date();
//        for (String key : modifyDetails.keySet()) {
//            eventToModify.setEventAttribute(key, modifyDetails.get(key));
//        }
        if (eventsToModify.isEmpty()) {
            return;
        }
        for (CalendarEvent ev: eventsToModify) {
            for (String key : modifyDetails.keySet()) {
                ev.setEventAttribute(key, modifyDetails.get(key));
            }
        }
        // eventToModify.setEventAttribute("lastModified", df.format(now));
        this.stringifyCalendar(); // Testing purposes
        this.saveToDisk(); // -> Impending functionality
    }

    // Input: A set of key-value pairs.
    // Modifies: Searches for the respective object in calendarEvents. If it exists, delete it.
    public void deleteEvent(HashMap<String, String> searchDetails) {
//        CalendarEvent eventToDelete = findEvent(searchDetails);
//        calendarEvents.remove(eventToDelete);
        ArrayList<CalendarEvent> eventsToDelete = findEvent(searchDetails);
        if (eventsToDelete.isEmpty()) {
            return;
        }
        for (CalendarEvent ev : eventsToDelete) {
            calendarEvents.remove(ev);
        }
        this.stringifyCalendar(); // Testing purposes
        this.saveToDisk(); // -> Impending functionality
    }

    // Need to handle repetition, so if the event field repeat is set to true, need to calculate how many events
    // from I guess now until the until field of the event -> and add that many events to the calendar
    // TODO: Need to figure out the Biweekly dependencies and libraries. On my machine, this is all red and I cannot get it to work
    public void exportCalendar(){
        ICalendar ical = new ICalendar();
        SimpleDateFormat df = new SimpleDateFormat("yyyy MM dd HHmm");
        // freq -> daily, weekly, monthly, yearly
        // until -> yyyy MM dd HHmm;
        // Change checks to ""
        for (CalendarEvent event: calendarEvents) {
            VEvent iCalEvent = new VEvent();

            if (!event.title.equals(""))
                iCalEvent.setSummary(event.title);
            if (!event.description.equals(""))
                iCalEvent.setDescription(event.description);
            if (!event.startDate.equals("")) {
                try {
                    iCalEvent.setDateStart(df.parse(event.startDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!event.endDate.equals("")) {
                try {
                    iCalEvent.setDateEnd(df.parse(event.endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!event.location.equals(""))
                iCalEvent.setLocation(event.location);
            if (!event.participants.equals("")) // May not be entirely consistent
                iCalEvent.addAttendee(event.participants);
            if (!event.lastModified.equals("")) {
                try {
                    iCalEvent.setLastModified(df.parse(event.lastModified));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!event.organizer.equals(""))
                iCalEvent.setOrganizer(event.organizer);
            if (!event.repeat.equals("false")){
                if (!event.frequency.equals("") && !event.until.equals("")) {
                    try {
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy MM dd");
                        Date until = df2.parse(event.until);
                        Recurrence recur = new Recurrence.Builder(getFrequency(event.frequency)).until(until).build();
                        iCalEvent.setRecurrenceRule(recur);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (!event.frequency.equals("") && event.until.equals("")) {
                    Recurrence recur = new Recurrence.Builder(getFrequency(event.frequency)).count(5).build();
                    iCalEvent.setRecurrenceRule(recur);
                }
            }
            ical.addEvent(iCalEvent);
        }

        File file = new File("testing_calendar.ics");

        // Delete calendar.json

        try {
            Biweekly.write(ical).go(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Frequency getFrequency(String freq){
        String input = freq.toLowerCase(Locale.ROOT);
        switch (input) {
            case "daily" : return Frequency.DAILY;
            case "weekly" : return Frequency.WEEKLY;
            case "monthly" : return Frequency.MONTHLY;
            case "yearly" : return Frequency.YEARLY;
            default : return Frequency.WEEKLY;
        }
    }
}
